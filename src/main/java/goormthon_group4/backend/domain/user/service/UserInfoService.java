package goormthon_group4.backend.domain.user.service;

import goormthon_group4.backend.domain.application.entity.Application;
import goormthon_group4.backend.domain.application.repository.ApplicationRepository;
import goormthon_group4.backend.domain.user.dto.request.UserInfoRequestDto;
import goormthon_group4.backend.domain.user.dto.response.MypageFullResponseDto;
import goormthon_group4.backend.domain.user.dto.response.MypageTeamDto;
import goormthon_group4.backend.domain.user.dto.response.UserInfoResponseDto;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.entity.UserInfo;
import goormthon_group4.backend.domain.user.repository.UserInfoRepository;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final ApplicationRepository applicationRepository;

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

    @Transactional(readOnly = true)
    public MypageFullResponseDto getMypage(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        // Lazy 로딩 트리거
        UserInfo userInfo = user.getUserInfo();
        if (userInfo != null) {
            userInfo.getNickname(); // 아무 필드나 호출하면 Lazy proxy가 초기화됨
        }


        // 해당 유저가 작성한 모든 지원서 조회
        List<Application> applications = applicationRepository.findAllByUser(user);

        // DTO로 변환 후 반환
        return MypageFullResponseDto.form(user, applications);
    }
}