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

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    public String getAuthenticatedEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Transactional
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

        return  new UserInfoResponseDto(userInfo);
    }

    public void updateUserInfo(UserInfoRequestDto dto) {
        String email = getAuthenticatedEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        UserInfo userInfo = user.getUserInfo();
        if(userInfo == null) {
            throw new RuntimeException("UserInfo를 찾을 수 없습니다.");
        }

        if (!userInfo.getNickname().equals(dto.getNickname()) &&
        userInfoRepository.existsByNickname(dto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
        userInfo.update(dto.getNickname(), dto.getMajor(), dto.getUniversity(), dto.getIntroduce());
    }
}