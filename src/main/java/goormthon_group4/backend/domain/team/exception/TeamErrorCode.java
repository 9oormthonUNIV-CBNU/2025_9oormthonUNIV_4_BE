package goormthon_group4.backend.domain.team.exception;

import goormthon_group4.backend.global.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TeamErrorCode implements BaseErrorCode {
  TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "팀을 찾을 수 없습니다."),
  PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "프로젝트를 찾을 수 없습니다."),
  OUTPUT_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "산출물을 찾을 수 없습니다."),
  OUTPUT_NOT_IN_TEAM(HttpStatus.NOT_FOUND, "T004", "해당 산출물은 요청한 팀에 속하지 않습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
