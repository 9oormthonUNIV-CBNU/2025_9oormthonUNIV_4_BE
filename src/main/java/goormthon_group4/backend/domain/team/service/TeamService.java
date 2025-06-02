package goormthon_group4.backend.domain.team.service;

import goormthon_group4.backend.domain.member.dto.MemberResponseDto;
import goormthon_group4.backend.domain.member.entity.Member;
import goormthon_group4.backend.domain.notify.dto.NotifySummaryDto;
import goormthon_group4.backend.domain.notify.service.NotifyService;
import goormthon_group4.backend.domain.project.entity.Project;
import goormthon_group4.backend.domain.project.repository.ProjectRepository;
import goormthon_group4.backend.domain.s3.service.S3Service;
import goormthon_group4.backend.domain.team.dto.request.TeamCreateRequest;
import goormthon_group4.backend.domain.team.dto.request.TeamUpdateOnlyStatusRequest;
import goormthon_group4.backend.domain.team.dto.request.TeamUpdateRequest;
import goormthon_group4.backend.domain.team.dto.response.MyTeamResponse;
import goormthon_group4.backend.domain.team.dto.response.TeamCreateResponse;
import goormthon_group4.backend.domain.team.dto.response.TeamDetailProjectResponse;
import goormthon_group4.backend.domain.team.dto.response.TeamDetailResponse;
import goormthon_group4.backend.domain.team.dto.response.TeamResponse;
import goormthon_group4.backend.domain.team.dto.response.TeamUpdateResponse;
import goormthon_group4.backend.domain.team.entity.Output;
import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.domain.team.entity.TeamStatus;
import goormthon_group4.backend.domain.team.exception.TeamErrorCode;
import goormthon_group4.backend.domain.team.repository.OutputRepository;
import goormthon_group4.backend.domain.team.repository.TeamRepository;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.entity.UserInfo;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.common.exception.CustomException;
import goormthon_group4.backend.global.common.exception.code.ErrorCode;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {
  private final TeamRepository teamRepository;
  private final UserRepository userRepository;
  private final OutputRepository outputRepository;
  private final ProjectRepository projectRepository;
  private final NotifyService notifyService;
  private final S3Service s3Service;

  private User getUserById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
  }

  private Project getProjectById(Long id) {
    return projectRepository.findById(id)
        .orElseThrow(() -> new CustomException(TeamErrorCode.PROJECT_NOT_FOUND));
  }

  private Team getTeamById(Long id) {
    return teamRepository.findById(id)
        .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));
  }

  // ✅ 공통 검증 로직
  private Team validateTeamLeader(Long teamId, Long userId) {
    Team team = getTeamById(teamId);
    if (!Objects.equals(team.getLeader().getId(), userId)) {
      throw new CustomException(ErrorCode.DONT_HAVE_GRANTED);
    }
    return team;
  }

  // ✅ 전체 필드 업데이트
  private void updateTeamFields(Team team, TeamUpdateRequest request) {
    team.setTitle(request.getTitle());
    team.setStatus(request.getStatus());
    team.setContent(request.getContent());
    team.setMaxUserCount(request.getMaxUserCount());
    team.setStartAt(request.getStartAt());
    team.setEndAt(request.getEndAt());
    team.setFileUrl(request.getFileUrl());
  }

  // ✅ 상태만 업데이트
  private void updateTeamStatus(Team team, TeamUpdateOnlyStatusRequest request) {
    team.setStatus(request.getStatus());
  }

  @Transactional
  public TeamCreateResponse create(Long leaderId, TeamCreateRequest requestDto) {
    User user = getUserById(leaderId);
    Project project = getProjectById(requestDto.getProjectId());

    // Team 엔티티 생성 (fileUrl 제외)
    Team team = Team.builder()
        .leader(user)
        .project(project)
        .status(TeamStatus.RECRUITING)
        .maxUserCount(requestDto.getMaxUserCount())
        .startAt(requestDto.getStartAt())
        .endAt(requestDto.getEndAt())
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .build();

    // fileUrl이 null이 아니면 설정
    if (requestDto.getFileUrl() != null) {
      team.setFileUrl(requestDto.getFileUrl());
    }

    Member leaderMember = Member.builder()
            .user(user)
            .team(team)
            .isLeader(true)
            .build();

    user.getMembers().add(leaderMember);
    team.getMembers().add(leaderMember);

    // 양방향 관계 설정
    user.addTeam(team);
    project.addTeam(team);


    teamRepository.save(team);

    return new TeamCreateResponse(team);
  }

  @Transactional
  public TeamUpdateResponse update(Long id, Long userId, TeamUpdateRequest request) {
    Team team = validateTeamLeader(id, userId);
    updateTeamFields(team, request); // 전체 업데이트
    return new TeamUpdateResponse(team);
  }

  @Transactional
  public TeamUpdateResponse updateOnlyStatus(Long id, Long userId, TeamUpdateOnlyStatusRequest request) {
    Team team = validateTeamLeader(id, userId);
    updateTeamStatus(team, request); // 상태만 업데이트
    return new TeamUpdateResponse(team);
  }

  @Transactional
  public void delete(Long userId,Long id) {
    Team team = getTeamById(id);
    if(!Objects.equals(team.getLeader().getId(), userId)) {
      throw new CustomException(ErrorCode.DONT_HAVE_GRANTED);
    }
    teamRepository.delete(team);
  }
  @Transactional
  public List<TeamResponse> getTeamsByProjectId(Long projectId) {
    // 프로젝트 존재 여부 먼저 확인
    boolean exists = projectRepository.existsById(projectId);
    if (!exists) {
      throw new CustomException(TeamErrorCode.PROJECT_NOT_FOUND);
    }
    return teamRepository.findTeamResponsesByProjectId(projectId);
  }

  @Transactional()
  public TeamDetailResponse getTeamDetail(Long teamId) {
    Team team = getTeamById(teamId);
    TeamDetailProjectResponse projectResponse = TeamDetailProjectResponse.from(team.getProject());

    List<MemberResponseDto> members = team.getMembers().stream()
            .filter(member -> member.getKickedAt() == null)
            .map(member -> {
              User user = member.getUser();
              UserInfo info = user.getUserInfo();
              return MemberResponseDto.builder()
                      .userId(user.getId())
                      .username(info.getNickname())
                      .imgUrl(info.getImgUrl())
                      .isLeader(member.isLeader())
                      .joinedDaysAgo((int) DAYS.between(member.getCreatedAt().toLocalDate(), LocalDate.now()))
                      .kickedAt(null)
                      .build();
            })
            .toList();

    // 공지사항 3개 페이징 조회
    Page<NotifySummaryDto> notifyPage = notifyService.getNoticesByTeam(teamId, 0, 3);
    List<NotifySummaryDto> notifies = notifyPage.getContent();

    return TeamDetailResponse.from(team, projectResponse, members.size(), notifies, members);
  }


  public List<MyTeamResponse> getTeamsByUserId(Long userId) {
    List<Team> teams = teamRepository.findAllTeamsInvolvingUser(userId);

    return teams.stream()
        .map(team -> MyTeamResponse.builder()
            .id(team.getId())
            .status(team.getStatus())
            .maxUserCount(team.getMaxUserCount())
            .startAt(team.getStartAt())
            .endAt(team.getEndAt())
            .title(team.getTitle())
            .content(team.getContent())
            .projectTitle(team.getProject() != null ? team.getProject().getTitle() : null)
            .build())
        .collect(Collectors.toList());
  }

  public String uploadFinalOutput(Long teamId, MultipartFile file, User user) {
    Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

    // 팀장인지 권한
    if (!team.getLeader().getId().equals(user.getId())) {
      throw new CustomException(ErrorCode.DONT_HAVE_GRANTED);
    }

    // 파일 업로드
    String fileUrl = s3Service.uploadFile(file);

    // Output 엔티티 생성 & 저장
    Output output = Output.builder()
            .team(team)
            .fileUrl(fileUrl)
            .build();

    outputRepository.save(output);

    return fileUrl;
  }

  @Transactional
  public void deleteOutput(Long teamId, Long outputId, User user) {
    Output output = outputRepository.findById(outputId)
            .orElseThrow(() -> new CustomException(TeamErrorCode.OUTPUT_NOT_FOUND));

    // 팀 ID 일치 여부 검증
    if (!output.getTeam().getId().equals(teamId)) {
      throw new CustomException(TeamErrorCode.OUTPUT_NOT_IN_TEAM);
    }

    // 권한 검사
    if (!output.getTeam().getLeader().getId().equals(user.getId())) {
      throw new CustomException(ErrorCode.DONT_HAVE_GRANTED);
    }

    outputRepository.delete(output);
  }

  @Transactional
  public void deleteAllOutputsByTeam(Long teamId, User user) {
    Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));

    if (!team.getLeader().getId().equals(user.getId())) {
      throw new CustomException(ErrorCode.FORBIDDEN);
    }

    List<Output> outputs = outputRepository.findAllByTeam(team);

    // S3에서도 삭제
    for (Output output : outputs) {
      s3Service.deleteFile(output.getFileUrl());
    }

    outputRepository.deleteAll(outputs);
  }

  // 팀 관리용 - 팀장 제외 모든 멤버, kicked 멤버도 포함
  @Transactional
  public List<MemberResponseDto> getManageableMembers(Long teamId, Long loginUserId) {
    Team team = getTeamById(teamId);

    if (!team.getLeader().getId().equals(loginUserId)) {
      throw new CustomException(ErrorCode.FORBIDDEN);
    }

    return team.getMembers().stream()
            .filter(member -> !member.isLeader()) // 팀장 제외
            .map(member -> {
              User user = member.getUser();
              UserInfo info = user.getUserInfo();

              return MemberResponseDto.builder()
                      .userId(user.getId())
                      .username(info.getNickname())
                      .imgUrl(info.getImgUrl())
                      .isLeader(false)
                      .joinedDaysAgo((int) DAYS.between(member.getCreatedAt().toLocalDate(), LocalDate.now()))
                      .kickedAt(member.getKickedAt())
                      .build();
            })
            .toList();
  }

}
