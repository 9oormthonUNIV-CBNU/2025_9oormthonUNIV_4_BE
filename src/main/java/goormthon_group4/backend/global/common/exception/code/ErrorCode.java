package goormthon_group4.backend.global.common.exception.code;

import goormthon_group4.backend.global.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseErrorCode {

  // 400 BAD REQUEST
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "400_001", "잘못된 요청입니다."),
  MISSING_REQUIRED_PARAMETER(HttpStatus.BAD_REQUEST, "400_002", "필수 파라미터가 누락되었습니다."),
  TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "400_003", "요청 파라미터 타입이 잘못되었습니다."),
  VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "400_004", "요청 값이 유효하지 않습니다."),
  INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "400_005", "잘못된 JSON 형식입니다."),
  CANNOT_REMOVE_LEADER(HttpStatus.BAD_REQUEST, "400_006", "팀장은 내보낼 수 없습니다."),

  // 401 UNAUTHORIZED
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "401_001", "인증이 필요합니다."),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "401_002", "유효하지 않은 토큰입니다."),

  // 403 FORBIDDEN
  FORBIDDEN(HttpStatus.FORBIDDEN, "403_001", "접근이 거부되었습니다."),

  // 404 NOT FOUND
  RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "404_001", "요청한 리소스를 찾을 수 없습니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404_002", "사용자를 찾을 수 없습니다."),
  NOTIFY_NOT_FOUND(HttpStatus.NOT_FOUND, "404_003", "공지사항을 찾을 수 없습니다."),
  TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "404_004", "팀을 찾을 수 없습니다."),
  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "404_005", "팀 멤버를 찾을 수 없습니다."),
  DONT_HAVE_GRANTED(HttpStatus.NOT_FOUND, "404", "팀장이 아닙니다."),

  // 409 CONFLICT
  DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "409_001", "이미 존재하는 리소스입니다."),

  // 500 INTERNAL SERVER ERROR
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500_001", "서버 내부 오류가 발생했습니다."),
  UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500_002", "알 수 없는 오류가 발생했습니다."),
  FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "500_003", "파일 업로드에 실패했습니다.");


  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
