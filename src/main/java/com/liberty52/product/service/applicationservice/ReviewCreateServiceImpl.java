package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.adapter.s3.S3UploaderApi;
import com.liberty52.product.global.exception.external.badrequest.BadRequestException;
import com.liberty52.product.global.exception.external.badrequest.ReviewAlreadyExistByOrderException;
import com.liberty52.product.global.exception.external.forbidden.NotYourOrderException;
import com.liberty52.product.global.exception.external.notfound.CustomProductNotFoundByIdException;
import com.liberty52.product.global.exception.external.notfound.OrderNotFoundByIdException;
import com.liberty52.product.global.exception.external.notfound.ProductNotFoundByNameException;
import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.service.controller.dto.ReplyCreateRequestDto;
import com.liberty52.product.service.controller.dto.ReviewCreateRequestDto;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.entity.Reply;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.entity.ReviewImage;
import com.liberty52.product.service.repository.CustomProductRepository;
import com.liberty52.product.service.repository.OrdersRepository;
import com.liberty52.product.service.repository.ProductRepository;
import com.liberty52.product.service.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewCreateServiceImpl implements ReviewCreateService {

  private final ReviewRepository reviewRepository;
  private final ProductRepository productRepository;
  private final OrdersRepository ordersRepository;
  private final CustomProductRepository customProductRepository;
  private final S3UploaderApi s3Uploader;

  @Override
  public void createReview(String reviewerId, ReviewCreateRequestDto dto, List<MultipartFile> images) {
    Product product = productRepository.findByName(dto.getProductName())
        .orElseThrow(() -> new ProductNotFoundByNameException(dto.getProductName()));

    Orders order = ordersRepository.findById(dto.getOrderId())
        .orElseThrow(() -> new OrderNotFoundByIdException(dto.getOrderId()));

    customProductRepository.findByOrderIdAndProductId(dto.getOrderId(), product.getId())
        .orElseThrow(() -> new CustomProductNotFoundByIdException(product.getId()));

    if (!(order.getAuthId().equals(reviewerId))) {
      throw new NotYourOrderException(reviewerId);
    }

    if (reviewRepository.findByOrder(order).isPresent()) {
      throw new ReviewAlreadyExistByOrderException();
    }

    Review review = Review.create(dto.getRating(), dto.getContent());
    review.associate(product);
    review.associate(order);

    if (images != null){
      addImage(images, review);
    }
    reviewRepository.save(review);
  }


  @Override
  public void createReply(String reviewerId, ReplyCreateRequestDto dto, String reviewId) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new ResourceNotFoundException("Review", "ID", reviewId));
    Reply reply = Reply.create(dto.getContent(), reviewerId);
    reply.associate(review);
  }


  private void addImage(List<MultipartFile> imageFiles, Review review) {
    if (imageFiles.size() > Review.IMAGES_MAX_COUNT)
      throw new BadRequestException(1 + " <= Size of images <= " + Review.IMAGES_MAX_COUNT);
    for (MultipartFile imageFile : imageFiles) {
      String reviewImageUrl = s3Uploader.upload(imageFile);
      review.addImage(ReviewImage.create(review, reviewImageUrl));
    }
  }
}
