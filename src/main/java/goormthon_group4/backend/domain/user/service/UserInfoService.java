package goormthon_group4.backend.domain.user.service;

import goormthon_group4.backend.domain.user.dto.request.UserInfoRequestDTO;
import goormthon_group4.backend.domain.user.dto.request.UserInfoResponseDTO;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.entity.UserInfo;
import goormthon_group4.backend.domain.user.repository.UserInfoRepository;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.Jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    public void saveUserInfo(UserInfoRequestDTO dto, String bearerToken) {
        String token = bearerToken.substring(7);
        Long userId = jwtProvider.getUserId(token);

        if (userInfoRepository.existsByNickname(dto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        UserInfo userInfo = UserInfo.builder()
                .nickname(dto.getNickname())
                .major(dto.getMajor())
                .university(dto.getUniversity())
                .introduce(dto.getIntroduce())
                .user(user)
                .build();

        userInfoRepository.save(userInfo);
    }

    public UserInfoResponseDTO getUserInfo(String bearerToken) {
        String token = extractToken(bearerToken);
        Long userId = jwtProvider.getUserId(token);

        UserInfo userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("UserInfo를 찾을 수 없습니다."));

        return  new UserInfoResponseDTO(userInfo);
    }

    public void updateUserInfo(UserInfoRequestDTO dto, String bearerToken) {
        String token = extractToken(bearerToken);
        Long userId = jwtProvider.getUserId(token);

        UserInfo userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("UserInfo를 찾을 수 없습니다."));

        if (!userInfo.getNickname().equals(dto.getNickname()) &&
        userInfoRepository.existsByNickname(dto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        userInfo.update(dto.getNickname(), dto.getMajor(), dto.getUniversity(), dto.getIntroduce());
    }

    private String extractToken(String bearerToken) {
        return bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;
    }
}