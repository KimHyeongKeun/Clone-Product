package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.ImageGenerationDto;

public interface ImageGenerationService {
    ImageGenerationDto.Response generateImage(String authId, ImageGenerationDto.Request dto);
}
