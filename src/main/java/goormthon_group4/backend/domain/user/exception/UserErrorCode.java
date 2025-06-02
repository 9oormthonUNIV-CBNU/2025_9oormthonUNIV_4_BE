package goormthon_group4.backend.domain.user.exception;

import goormthon_group4.backend.global.common.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements BaseErrorCode {
    USERINFO_NOT_FOUND(HttpStatus.NOT_FOUND, "404_001", "유저 정보를 찾을 수 없습니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "400_001", "이미 사용 중인 닉네임입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404_002", "사용자를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
