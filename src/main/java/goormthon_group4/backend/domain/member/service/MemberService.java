package goormthon_group4.backend.domain.member.service;

import goormthon_group4.backend.domain.member.dto.MemberProfileDto;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.entity.UserInfo;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.common.exception.CustomException;
import goormthon_group4.backend.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public MemberProfileDto getMemberProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserInfo info = user.getUserInfo();

        return MemberProfileDto.builder()
                .nickname(info.getNickname())
                .imgUrl(info.getImgUrl())
                .university(info.getUniversity())
                .major(info.getMajor())
                .introduce(info.getIntroduce())
                .build();
    }
}
