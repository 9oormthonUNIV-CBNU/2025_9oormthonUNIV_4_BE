package goormthon_group4.backend.domain.user.service;

import goormthon_group4.backend.domain.user.dto.UserInfoRequestDto;
import goormthon_group4.backend.domain.user.dto.UserInfoResponseDto;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.entity.UserInfo;
import goormthon_group4.backend.domain.user.repository.UserInfoRepository;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.origin.Origin.from;

@Transactional
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    // 인증된 사용자의 이메일 가져오기
    private String getAuthenticatedEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public void saveUserInfo(UserInfoRequestDto dto) {
        String email = getAuthenticatedEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        if (userInfoRepository.existsByNickname(dto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        UserInfo userInfo = UserInfo.builder()
                .nickname(dto.getNickname())
                .major(dto.getMajor())
                .university(dto.getUniversity())
                .introduce(dto.getIntroduce())
                .user(user)
                .build();
        // 양방향 연관관계 설정
        user.setUserInfo(userInfo);

        // 둘 중 하나만 save하면 됨 (cascade = ALL 설정 시)
        userRepository.save(user); // user -> userInfo 같이 저장됨


        userInfoRepository.save(userInfo);
    }

    public UserInfoResponseDto getUserInfo() {
        String email = getAuthenticatedEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        UserInfo userInfo = user.getUserInfo();
        if (userInfo == null) {
            throw new RuntimeException("UserInfo를 찾을 수 없습니다.");
        }

        return new UserInfoResponseDto(userInfo);
    }

    @Transactional
    public void updateUserInfo(UserInfoRequestDto dto) {
        String email = getAuthenticatedEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        UserInfo userInfo = user.getUserInfo();
        if (userInfo == null) {
            throw new RuntimeException("UserInfo를 찾을 수 없습니다.");
        }

        if (!userInfo.getNickname().equals(dto.getNickname()) &&
                userInfoRepository.existsByNickname(dto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        userInfo.update(dto.getNickname(), dto.getMajor(), dto.getUniversity(), dto.getIntroduce());
    }
}