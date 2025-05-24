package goormthon_group4.backend.domain.univcert.service;

import com.univcert.api.UnivCert;
import goormthon_group4.backend.domain.univcert.dto.UnivCodeRequestDTO;
import goormthon_group4.backend.domain.univcert.dto.UnivRequestDTO;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.utils.AuthenticatedUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnivCertService {

    private final AuthenticatedUserUtils authenticatedUserUtils;

    private final static boolean univCheck = true;
    private final UserRepository userRepository;

    @Value("${univcert.api-key")
    private String univCertApiKey;

    public String univCertify(UnivRequestDTO dto, User user) throws IOException {

        Map<String, Object> response = UnivCert.certify(
                univCertApiKey,
                dto.getUnivEmail(),
                dto.getUnivName(),
                univCheck
        );
        validate(response);
        return "인증 코드가 이메일로 전송되었습니다.";
    }

    @Transactional
    public String univCertifyCode(UnivCodeRequestDTO dto, User user) throws IOException {
        Map<String, Object> response = UnivCert.certifyCode(
                univCertApiKey,
                dto.getUnivEmail(),
                dto.getUnivName(),
                dto.getCode()
        );

        validateAndAuthenticate(response, user);
        return "대학 인증에 성공하였습니다.";
    }

    private void validate(Map<String, Object> response) {
        boolean success = (boolean) response.get("success");
        String message = (String) response.get("message");
        if(!success) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateAndAuthenticate(Map<String, Object> response, User user) {
        boolean success = (boolean) response.get("success");
        String message = (String) response.get("message");
        if (success) {
            user.authenticatedUniversity();
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException(message);
        }
    }
}
