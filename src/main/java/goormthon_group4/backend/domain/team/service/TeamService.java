package goormthon_group4.backend.domain.team.service;

import goormthon_group4.backend.domain.team.dto.request.TeamCreateRequest;
import goormthon_group4.backend.domain.team.dto.response.TeamCreateResponse;
import goormthon_group4.backend.domain.team.repository.TeamRepository;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {
  private final TeamRepository teamRepository;
  private final UserRepository userRepository;

  @Transactional
  public TeamCreateResponse create(TeamCreateRequest requestDto) {
    return TeamCreateResponse.builder().build();
  }
}
