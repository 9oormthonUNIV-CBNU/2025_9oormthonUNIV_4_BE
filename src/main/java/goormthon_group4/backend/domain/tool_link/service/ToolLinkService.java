package goormthon_group4.backend.domain.tool_link.service;

import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.domain.team.exception.TeamErrorCode;
import goormthon_group4.backend.domain.team.repository.TeamRepository;
import goormthon_group4.backend.domain.tool_link.dto.request.ToolLinkCreateRequest;
import goormthon_group4.backend.domain.tool_link.dto.response.ToolLinkResponse;
import goormthon_group4.backend.domain.tool_link.entity.ToolLink;
import goormthon_group4.backend.domain.tool_link.repository.ToolLinkRepository;
import goormthon_group4.backend.global.common.exception.CustomException;
import goormthon_group4.backend.global.common.exception.code.ErrorCode;
import goormthon_group4.backend.global.module.MetadataExtractor;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ToolLinkService {

  private static final int PAGE_SIZE = 3;

  private final ToolLinkRepository toolLinkRepository;
  private final TeamRepository teamRepository;

  private Team getTeamById(Long id) {
    return teamRepository.findById(id)
        .orElseThrow(() -> new CustomException(TeamErrorCode.TEAM_NOT_FOUND));
  }


  @Transactional()
  public Page<ToolLinkResponse> getToolLinks(int page) {
    Pageable pageable = PageRequest.of(page, PAGE_SIZE);
    Page<ToolLink> toolLinks = toolLinkRepository.findAll(pageable);
    return toolLinks.map(this::mapToResponse);
  }

  @Transactional()
  public List<ToolLinkResponse> getAllToolLinks() {
    List<ToolLink> toolLinks = toolLinkRepository.findAll();
    return toolLinks.stream().map(this::mapToResponse).collect(Collectors.toList());
  }

  @Transactional
  public void deleteToolLinks(List<Long> ids, Long userId, Long teamId) {
    Team team = getTeamById(teamId);

    if (!Objects.equals(team.getLeader().getId(), userId)) {
      throw new CustomException(ErrorCode.DONT_HAVE_GRANTED);
    }


    toolLinkRepository.deleteAllByIdInBatch(ids);
  }

  @Transactional
  public ToolLinkResponse createToolLink(
      Long teamId,
      ToolLinkCreateRequest toolLinkCreateRequest,
      Long userId
  ) {

    Team team = getTeamById(teamId);
    if (!Objects.equals(team.getLeader().getId(), userId)) {
      throw new CustomException(ErrorCode.DONT_HAVE_GRANTED);
    }

    String title = MetadataExtractor.extractTitle(toolLinkCreateRequest.getToolLink());
    String imgUrl = MetadataExtractor.extractImage(toolLinkCreateRequest.getToolLink());


    ToolLink toolLink = ToolLink.builder()
        .title(title)
        .imgUrl(imgUrl)
        .toolLink(toolLinkCreateRequest.getToolLink())
        .team(team)
        .build();

    toolLinkRepository.save(toolLink);

    return ToolLinkResponse.builder()
        .id(toolLink.getId())
        .title(toolLink.getTitle())
        .toolLink(toolLink.getToolLink())
        .imgUrl(toolLink.getImgUrl())
        .build();
  }

  private ToolLinkResponse mapToResponse(ToolLink toolLink) {
    return ToolLinkResponse.builder()
        .id(toolLink.getId())
        .title(toolLink.getTitle())
        .toolLink(toolLink.getToolLink())
        .imgUrl(toolLink.getImgUrl())
        .build();
  }

}