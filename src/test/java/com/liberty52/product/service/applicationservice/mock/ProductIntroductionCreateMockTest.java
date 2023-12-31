package com.liberty52.product.service.applicationservice.mock;

import static com.liberty52.product.global.constants.RoleConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.liberty52.product.global.adapter.s3.S3UploaderApi;
import com.liberty52.product.global.exception.external.badrequest.BadRequestException;
import com.liberty52.product.global.exception.internal.S3UploaderException;
import com.liberty52.product.service.applicationservice.impl.ProductIntroductionCreateServiceImpl;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductIntroductionCreateMockTest {
	@InjectMocks
	ProductIntroductionCreateServiceImpl productService;

	@Mock
	ProductRepository productRepository;

	@Mock
	S3UploaderApi s3Uploader;

	@Test
	void createProductIntroductionWhenImageNotRegistered() {
		// Given
		String productId = "testProductId";
		MultipartFile multipartFile = mock(MultipartFile.class);

		Product mockProduct = mock(Product.class);
		given(productRepository.findById(anyString())).willReturn(Optional.of(mockProduct));
		given(mockProduct.getProductIntroductionImageUrl()).willReturn(null); // 처음에는 이미지 URL이 없다고 가정
		given(s3Uploader.upload(multipartFile)).willReturn("mockImageUrl");

		// When
		productService.createProductIntroduction(ADMIN, productId, multipartFile);

		// Then: 검증 로직 추가 (예: createProductIntroduction 메소드 호출 확인)
		verify(mockProduct).createProductIntroduction("mockImageUrl");
	}

	@Test
	void createProductIntroductionWhenImageAlreadyRegistered() {
		// Given
		String productId = "testProductId";
		MultipartFile multipartFile = mock(MultipartFile.class);
		// When
		Product mockProduct = mock(Product.class);
		given(productRepository.findById(anyString())).willReturn(Optional.of(mockProduct));
		given(mockProduct.getProductIntroductionImageUrl()).willReturn("mockImageUrl");
		// Then: 검증 로직 추가 (예: 이미지가 등록되어 있을 때 예외가 발생하는지 확인)
		assertThrows(BadRequestException.class, () -> productService.createProductIntroduction(ADMIN, productId, multipartFile));
	}

}
