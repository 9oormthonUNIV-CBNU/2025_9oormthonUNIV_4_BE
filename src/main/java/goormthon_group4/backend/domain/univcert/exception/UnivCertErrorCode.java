package goormthon_group4.backend.domain.univcert.exception;

import goormthon_group4.backend.global.common.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UnivCertErrorCode implements BaseErrorCode {
    UNIV_CERTIFY_FAILED(HttpStatus.BAD_REQUEST, "400_101", "학교 인증 실패"),
    UNIV_CODE_FAILED(HttpStatus.BAD_REQUEST, "400_102", "인증 코드 확인 실패"),
    UNIV_CLEAR_FAILED(HttpStatus.BAD_REQUEST, "400_103", "학교 인증 초기화 실패"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404_101", "유저를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
