package com.liberty52.product.service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.liberty52.product.service.applicationservice.LicenseImageRetrieveService;
import com.liberty52.product.service.controller.dto.LicenseImageRetrieveDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LicenseImageRetrieveController {
	private final LicenseImageRetrieveService licenseImageRetrieveService;

	@GetMapping("/admin/licenseImage")
	@ResponseStatus(HttpStatus.OK)
	public List<LicenseImageRetrieveDto> retrieveLicenseImages(@RequestHeader("LB-Role") String role) {
		return licenseImageRetrieveService.retrieveLicenseImages(role);
	}
	


}
