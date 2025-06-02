package goormthon_group4.backend.global.common.exception;

import goormthon_group4.backend.domain.univcert.exception.UnivCertErrorCode;
import goormthon_group4.backend.global.common.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final BaseErrorCode errorCode;

    public CustomException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String message) {
        super(errorCode.getMessage() + message);
        this.errorCode = errorCode;
    }

    public CustomException(UnivCertErrorCode univCertErrorCode, String message) {
        super(univCertErrorCode.getMessage() + message);
        this.errorCode = univCertErrorCode;
    }
}