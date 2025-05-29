package goormthon_group4.backend.domain.team.service;

import goormthon_group4.backend.domain.project.entity.Project;
import goormthon_group4.backend.domain.project.repository.ProjectRepository;
import goormthon_group4.backend.domain.team.dto.request.TeamCreateRequest;
import goormthon_group4.backend.domain.team.dto.response.TeamCreateResponse;
import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.domain.team.entity.TeamStatus;
import goormthon_group4.backend.domain.team.exception.TeamErrorCode;
import goormthon_group4.backend.domain.team.repository.TeamRepository;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.common.exception.CustomException;
import goormthon_group4.backend.global.common.exception.code.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
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

    userRepository.save(user);
    teamRepository.save(team);

    return TeamCreateResponse.builder()
        .id(team.getId())
        .status(team.getStatus())
        .title(team.getTitle())
        .content(team.getContent())
        .fileUrl(team.getFileUrl())
        .startAt(team.getStartAt())
        .endAt(team.getEndAt())
        .maxUserCount(team.getMaxUserCount())
        .build();
  }
}
