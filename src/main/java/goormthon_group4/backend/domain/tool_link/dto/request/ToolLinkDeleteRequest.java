package goormthon_group4.backend.domain.tool_link.dto.request;

import java.util.List;
import lombok.Getter;

@Getter
public class ToolLinkDeleteRequest {
  private List<Long> ids;
}