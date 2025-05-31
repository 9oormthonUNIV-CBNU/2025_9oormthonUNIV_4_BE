package goormthon_group4.backend.domain.member.service;

import goormthon_group4.backend.domain.member.dto.MemberProfileDto;
import goormthon_group4.backend.domain.member.entity.Member;
import goormthon_group4.backend.domain.member.repository.MemberRepository;
import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.domain.team.repository.TeamRepository;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.entity.UserInfo;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.common.exception.CustomException;
import goormthon_group4.backend.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;


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

    @Transactional
    public void kickMemberFromTeam(Long teamId, Long targetUserId, Long requesterUserId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        User requester = userRepository.findById(requesterUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 요청자가 팀 멤버인지 확인
        Member requesterMember = memberRepository.findByTeamAndUser(team, requester)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!requesterMember.isLeader()) {
            throw new CustomException(ErrorCode.FORBIDDEN); // 팀장이 아님
        }

        if (requesterUserId.equals(targetUserId)) {
            throw new CustomException(ErrorCode.CANNOT_REMOVE_LEADER);
        }

        Member targetMember = memberRepository.findByTeamAndUser(team, targetUser)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        targetMember.kickOut();
    }


}
