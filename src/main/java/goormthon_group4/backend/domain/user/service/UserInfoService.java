package goormthon_group4.backend.domain.user.service;

import goormthon_group4.backend.domain.application.entity.Application;
import goormthon_group4.backend.domain.application.repository.ApplicationRepository;
import goormthon_group4.backend.domain.user.dto.request.UserInfoRequestDto;
import goormthon_group4.backend.domain.user.dto.response.MypageFullResponseDto;
import goormthon_group4.backend.domain.user.dto.response.UserInfoResponseDto;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.entity.UserInfo;
import goormthon_group4.backend.domain.user.exception.UserErrorCode;
import goormthon_group4.backend.domain.user.repository.UserInfoRepository;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import goormthon_group4.backend.global.common.exception.CustomException;
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
            throw new CustomException(UserErrorCode.DUPLICATE_NICKNAME);
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
                .orElseThrow(()-> new CustomException(UserErrorCode.USER_NOT_FOUND));

        return new UserInfoResponseDto(userInfo);
    }

    public void updateUserInfo(UserInfoRequestDto userInfoRequestDto, CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        UserInfo userInfo = userInfoRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        if (!userInfo.getNickname().equals(userInfoRequestDto.getNickname()) &&
                userInfoRepository.existsByNickname(userInfoRequestDto.getNickname())) {
            throw new CustomException(UserErrorCode.DUPLICATE_NICKNAME);
        }

        userInfo.update(
                userInfoRequestDto.getNickname(),
                userInfoRequestDto.getMajor(),
                userInfoRequestDto.getUniversity(),
                userInfoRequestDto.getIntroduce(),
                userInfoRequestDto.getImgUrl());
    }

    @Transactional
    public MypageFullResponseDto getMypage(User user) {

        // 세션 안에서 다시 영속화
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));


        // 해당 유저가 작성한 모든 지원서 조회
        List<Application> applications = applicationRepository.findAllByUser(managedUser);

        // DTO로 변환 후 반환
        return MypageFullResponseDto.from(user, applications);
    }
}