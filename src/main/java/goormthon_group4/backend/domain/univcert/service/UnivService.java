package goormthon_group4.backend.domain.univcert.service;

import com.univcert.api.UnivCert;
import goormthon_group4.backend.domain.univcert.dto.UnivCodeRequestDto;
import goormthon_group4.backend.domain.univcert.dto.UnivRequestDto;
import goormthon_group4.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UnivService {
    // true : 대학 재학, false : 메일 소유
    private final static boolean univ_check = true;

    @Value("${univ.key}")
    private String univCertAPIKey;

    public String univCertify(UnivRequestDto univRequestDto, User user) throws IOException {
        Map<String, Object> response = UnivCert.certify(
                univCertAPIKey,
                univRequestDto.getUnivEmail(),
                univRequestDto.getUnivName(),
                univ_check
        );
        validate(response);
        return "인증코드가 이메일로 전송되었습니다.";
    }

    private void validate(Map<String, Object> response) {
        boolean success = (boolean) response.get("success");
        String message = (String) response.get("message");
        if(!success) {
            throw new IllegalArgumentException("학교 인증 실패 : " + message);
        }
    }

    public String univCertifyCode(UnivCodeRequestDto univCodeRequestDto, User user) throws IOException {
        Map<String, Object> response = UnivCert.certifyCode(
                univCertAPIKey,
                univCodeRequestDto.getUnivEmail(),
                univCodeRequestDto.getUnivName(),
                univCodeRequestDto.getCode()
        );
        validateCodeResponse(response, user);
        return "인증이 성공하였습니다.";
    }

    public void validateCodeResponse(Map<String, Object> response, User user) {
        boolean success = (boolean) response.get("success");
        String message = (String) response.get("message");
        if(!success) {
            throw new IllegalArgumentException("인증 코드 확인 실패 : " + message);
        }
        user.authenticateUniversity();
    }
}
