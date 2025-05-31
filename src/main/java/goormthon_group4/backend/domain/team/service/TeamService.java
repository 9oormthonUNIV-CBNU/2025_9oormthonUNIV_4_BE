package goormthon_group4.backend.domain.team.service;

import goormthon_group4.backend.domain.project.entity.Project;
import goormthon_group4.backend.domain.project.repository.ProjectRepository;
import goormthon_group4.backend.domain.team.dto.request.TeamCreateRequest;
import goormthon_group4.backend.domain.team.dto.request.TeamUpdateRequest;
import goormthon_group4.backend.domain.team.dto.response.MyTeamResponse;
import goormthon_group4.backend.domain.team.dto.response.TeamCreateResponse;
import goormthon_group4.backend.domain.team.dto.response.TeamDetailProjectResponse;
import goormthon_group4.backend.domain.team.dto.response.TeamDetailResponse;
import goormthon_group4.backend.domain.team.dto.response.TeamResponse;
import goormthon_group4.backend.domain.team.dto.response.TeamUpdateResponse;
import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.domain.team.entity.TeamStatus;
import goormthon_group4.backend.domain.team.exception.TeamErrorCode;
import goormthon_group4.backend.domain.team.repository.TeamRepository;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.common.exception.CustomException;
import goormthon_group4.backend.global.common.exception.code.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {
  private final TeamRepository teamRepository;
  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;

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

    // 양방향 연관관계 설정
    user.addTeam(team);
    project.addTeam(team);

    teamRepository.save(team);

    return new TeamCreateResponse(team);
  }

  @Transactional
  public TeamUpdateResponse update(Long id, Long userId, TeamUpdateRequest request) {
    Team team = getTeamById(id);

    if (!Objects.equals(team.getLeader().getId(), userId)) {
      throw new CustomException(ErrorCode.DONT_HAVE_GRANTED);
    }

    // 요청 값을 기반으로 팀 정보 업데이트
    team.setTitle(request.getTitle());
    team.setContent(request.getContent());
    team.setMaxUserCount(request.getMaxUserCount());
    team.setStartAt(request.getStartAt());
    team.setEndAt(request.getEndAt());
    team.setFileUrl(request.getFileUrl());

    // 응답 객체로 변환하여 반환
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
    return TeamDetailResponse.from(team, projectResponse, team.getMembers().size());
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


}
