package goormthon_group4.backend.domain.univcert.service;

import com.univcert.api.UnivCert;
import goormthon_group4.backend.domain.univcert.dto.UnivCodeRequestDto;
import goormthon_group4.backend.domain.univcert.dto.UnivRequestDto;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UnivService {
    // true : 대학 재학, false : 메일 소유
    private final static boolean univ_check = true;
    private final UserRepository userRepository;

    @Value("${univ.key}")
    private String univCertAPIKey;

    public String univCertify(UnivRequestDto univRequestDto, CustomUserDetails customUserDetails) throws IOException {
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

    @Transactional
    public String univCertifyCode(UnivCodeRequestDto univCodeRequestDto, CustomUserDetails customUserDetails) throws IOException {
        Map<String, Object> response = UnivCert.certifyCode(
                univCertAPIKey,
                univCodeRequestDto.getUnivEmail(),
                univCodeRequestDto.getUnivName(),
                univCodeRequestDto.getCode()
        );
        validateCodeResponse(response, customUserDetails);
        return "인증이 성공하였습니다.";
    }

    public void validateCodeResponse(Map<String, Object> response, CustomUserDetails customUserDetails) throws IOException {
        boolean success = (boolean) response.get("success");
        String message = (String) response.get("message");
        if(!success) {
            throw new IllegalArgumentException("인증 코드 확인 실패 : " + message);
        }
        User user = userRepository.findByEmail(customUserDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        user.authenticateUniversity();
    }

    @Transactional
    public String univClear(UnivRequestDto univRequestDto, CustomUserDetails customUserDetails) throws IOException {
        Map<String, Object> response = UnivCert.clear(univCertAPIKey, univRequestDto.getUnivEmail());

        boolean success = (boolean) response.get("success");
        String message = (String) response.get("message");

        if (!success) {
            throw new IllegalArgumentException("학교 인증 초기화 실패 : " + message);
        }

        return "학교 인증 정보가 초기화되었습니다.";
    }
}
