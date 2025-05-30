package goormthon_group4.backend.domain.user.service;

import goormthon_group4.backend.domain.user.dto.request.UserInfoRequestDto;
import goormthon_group4.backend.domain.user.dto.response.UserInfoResponseDto;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.entity.UserInfo;
import goormthon_group4.backend.domain.user.repository.UserInfoRepository;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    public void saveUserInfo(UserInfoRequestDto userInfoRequestDto, CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        if (userInfoRepository.existsByNickname(userInfoRequestDto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        UserInfo userInfo = UserInfo.builder()
                .nickname(userInfoRequestDto.getNickname())
                .major(userInfoRequestDto.getMajor())
                .university(userInfoRequestDto.getUniversity())
                .introduce(userInfoRequestDto.getIntroduce())
                .imgUrl(userInfoRequestDto.getImgUrl())
                .user(user)
                .build();

        user.setUserInfo(userInfo);
        userRepository.save(user);
    }

    public UserInfoResponseDto getUserInfo(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        UserInfo userInfo = userInfoRepository.findByUser(user)
                .orElseThrow(()-> new RuntimeException("UserInfo를 찾을 수 없습니다."));

        return new UserInfoResponseDto(userInfo);
    }

    public void updateUserInfo(UserInfoRequestDto userInfoRequestDto, CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        UserInfo userInfo = userInfoRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("UserInfo를 찾을 수 없습니다."));

        if (!userInfo.getNickname().equals(userInfoRequestDto.getNickname()) &&
                userInfoRepository.existsByNickname(userInfoRequestDto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        userInfo.update(
                userInfoRequestDto.getNickname(),
                userInfoRequestDto.getMajor(),
                userInfoRequestDto.getUniversity(),
                userInfoRequestDto.getIntroduce(),
                userInfoRequestDto.getImgUrl());
    }
}