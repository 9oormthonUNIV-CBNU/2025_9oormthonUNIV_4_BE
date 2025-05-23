package goormthon_group4.backend.global.common.exception;

import goormthon_group4.backend.global.common.exception.code.ErrorCode;
import lombok.Builder;
import lombok.Getter;

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
