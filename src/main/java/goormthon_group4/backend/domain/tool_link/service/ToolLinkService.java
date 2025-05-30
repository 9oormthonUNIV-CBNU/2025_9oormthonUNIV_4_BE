package goormthon_group4.backend.domain.tool_link.service;

import goormthon_group4.backend.domain.tool_link.dto.response.ToolLinkResponse;
import goormthon_group4.backend.domain.tool_link.entity.ToolLink;
import goormthon_group4.backend.domain.tool_link.repository.ToolLinkRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToolLinkService {

  private static final int PAGE_SIZE = 3;

  private final ToolLinkRepository toolLinkRepository;

  @Transactional()
  public Page<ToolLinkResponse> getToolLinks(int page) {
    Pageable pageable = PageRequest.of(page, PAGE_SIZE);
    Page<ToolLink> toolLinks = toolLinkRepository.findAll(pageable);
    return toolLinks.map(this::mapToResponse);
  }

  private ToolLinkResponse mapToResponse(ToolLink toolLink) {
    return ToolLinkResponse.builder()
        .id(toolLink.getId())
        .title(toolLink.getTitle())
        .imgUrl(toolLink.getImgUrl())
        .build();
  }

}