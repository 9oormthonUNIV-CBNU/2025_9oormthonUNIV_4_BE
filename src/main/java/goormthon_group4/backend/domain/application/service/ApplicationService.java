package goormthon_group4.backend.domain.application.service;

import goormthon_group4.backend.domain.application.dto.request.ApplicationRequestDto;
import goormthon_group4.backend.domain.application.dto.request.ApplicationStatusUpdateRequest;
import goormthon_group4.backend.domain.application.dto.response.ApplicationResponseDto;
import goormthon_group4.backend.domain.application.entity.Application;
import goormthon_group4.backend.domain.application.entity.ApplicationStatus;
import goormthon_group4.backend.domain.application.repository.ApplicationRepository;
import goormthon_group4.backend.domain.member.entity.Member;
import goormthon_group4.backend.domain.s3.service.S3Service;
import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.domain.team.exception.TeamErrorCode;
import goormthon_group4.backend.domain.team.repository.TeamRepository;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import goormthon_group4.backend.global.common.exception.CustomException;
import goormthon_group4.backend.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final S3Service s3Service;

    @Transactional
    public ApplicationResponseDto submitApplication(Long teamId,
                                                    ApplicationRequestDto requestDto,
                                                    CustomUserDetails userDetails,
                                                    MultipartFile file) {
        Team team = getTeamOrThrow(teamId);
        User user = userDetails.getUser();

        String fileUrl = uploadFileIfPresent(file);

        Application application = Application.builder()
                .status(ApplicationStatus.PENDING)
                .team(team)
                .user(user)
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .phoneNumber(requestDto.getPhoneNumber())
                .introduce(requestDto.getIntroduce())
                .purpose(requestDto.getPurpose())
                .skillExperience(requestDto.getSkillExperience())
                .strengthsExperience(requestDto.getStrengthsExperience())
                .additionalInfo(requestDto.getAdditionalInfo())
                .fileUrl(fileUrl)
                .build();

        applicationRepository.save(application);
        return ApplicationResponseDto.from(application);
    }

    @Transactional(readOnly = true)
    public ApplicationResponseDto getApplication(Long teamId, Long userId, CustomUserDetails userDetails) {
        Team team = getTeamOrThrow(teamId);
        checkTeamLeader(team, userDetails.getUser());

        User user = getUserOrThrow(userId);

        Application application = applicationRepository.findByUserAndTeam(user, team)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "지원서를 찾을 수 없습니다."));

        return ApplicationResponseDto.from(application);
    }

    @Transactional
    public List<ApplicationResponseDto> getApplications(Long teamId, CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

        // 팀장 권한 확인
        if (!team.getLeader().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.DONT_HAVE_GRANTED);
        }

        // 팀에 속한 모든 지원서 조회
        List<Application> applications = applicationRepository.findAllByTeam(team);

        // DTO 변환
        return applications.stream()
                .map(ApplicationResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponseDto> getApplicationsByTeamId(Long teamId, CustomUserDetails userDetails) {
        Team team = getTeamOrThrow(teamId);
        checkTeamLeader(team, userDetails.getUser());

        return applicationRepository.findAllByTeam(team).stream()
                .map(ApplicationResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApplicationStatusUpdateRequest updateApplication(Long teamId, Long userId,
                                                            ApplicationStatusUpdateRequest request,
                                                            CustomUserDetails userDetails) {
        Team team = getTeamOrThrow(teamId);
        checkTeamLeader(team, userDetails.getUser());

        User user = getUserOrThrow(userId);

        Application application = applicationRepository.findByUserAndTeam(user, team)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "지원서를 찾을 수 없습니다."));

        application.changeStatus(request.getStatus());

        if (request.getStatus() == ApplicationStatus.ACCEPT) {
            // 이미 등록된 멤버인지 확인
            boolean alreadyMember = team.getMembers().stream()
                    .anyMatch(member -> member.getUser().getId().equals(user.getId()));

            if (!alreadyMember) {
                Member member = Member.builder()
                        .user(user)
                        .team(team)
                        .build();

                // 양방향 관계 모두 추가
                user.getMembers().add(member);
                team.getMembers().add(member);
            }
        }
        return request;
    }

    // =========================== ⬇️ 헬퍼 메서드 정리 ⬇️ ===========================

    private Team getTeamOrThrow(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "팀을 찾을 수 없습니다."));
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void checkTeamLeader(Team team, User user) {
        if (!team.getLeader().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN, "팀장만 지원서에 접근할 수 있습니다.");
        }
    }

    private String uploadFileIfPresent(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            return s3Service.uploadFile(file);
        }
        return null;
    }
}