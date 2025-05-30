package goormthon_group4.backend.domain.s3.controller;

import goormthon_group4.backend.domain.s3.service.S3Service;
import goormthon_group4.backend.global.common.exception.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/s3")
public class S3Controller {

  private final S3Service s3Service;

  public S3Controller(S3Service s3Service) {
    this.s3Service = s3Service;
  }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
  public ApiResponse<String> uploadImage(@RequestPart MultipartFile file) {
      String url = s3Service.uploadFile(file);
      return ApiResponse.success(url);
  }
}