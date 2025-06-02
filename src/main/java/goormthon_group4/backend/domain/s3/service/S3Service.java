package goormthon_group4.backend.domain.s3.service;

import goormthon_group4.backend.global.common.exception.CustomException;
import goormthon_group4.backend.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedFileUpload;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;



import java.io.File;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

  @Value("${cloud.s3.bucket}")
  private String bucket;

  private final S3Client s3Client;
  private final S3TransferManager transferManager;


  public String uploadFile(MultipartFile file)  {

    try {
      String originalName = Optional.ofNullable(file.getOriginalFilename()).orElse("file");
      String fileName = UUID.randomUUID() + "_" + originalName;
      // MultipartFile -> File 변환
      File tempFile = File.createTempFile("upload-", originalName);
      file.transferTo(tempFile);
      tempFile.deleteOnExit();

      // UploadFileRequest 생성
      UploadFileRequest uploadFileRequest = UploadFileRequest
          .builder()
          .putObjectRequest(b -> b.bucket(bucket).key(fileName))
          .source(tempFile.toPath()).build();

      FileUpload fileUpload = transferManager.uploadFile(uploadFileRequest);
      CompletedFileUpload uploadResult = fileUpload.completionFuture().join();

      // ✅ S3 URL 생성해서 반환
      String fileUrl = s3Client
          .utilities()
          .getUrl(b -> b.bucket(bucket).key(fileName))
          .toExternalForm();

      return fileUrl;
    } catch (Exception e) {
      e.printStackTrace();
      throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
    }

  }

  public void deleteFile(String fileUrl) {
    try {
      // s3 버킷에서 key 추출
      String key = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

      // s3에서 삭제
      s3Client.deleteObject(b -> b
              .bucket(bucket)
              .key(key)
      );
    } catch (Exception e) {
      throw new CustomException(ErrorCode.FILE_DELETE_FAILED);
    }
  }
}
