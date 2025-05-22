package goormthon_group4.backend.global.common.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorResponse {
  private final int status;
  private final String code;
  private final String message;

  public static ErrorResponse from(ErrorCode errorCode) {
    return ErrorResponse.builder()
        .status(errorCode.getHttpStatus().value())
        .code(errorCode.getCode())
        .message(errorCode.getMessage())
        .build();
  }
}
