package goormthon_group4.backend.domain.tool_link.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ToolLinkResponse {
  private Long id;
  private String title;
  private String imgUrl;
}
