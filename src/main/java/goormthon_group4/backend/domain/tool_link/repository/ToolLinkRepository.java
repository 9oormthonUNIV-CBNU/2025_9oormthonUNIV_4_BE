package goormthon_group4.backend.domain.tool_link.repository;

import goormthon_group4.backend.domain.tool_link.entity.ToolLink;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolLinkRepository extends JpaRepository<ToolLink, Long> {
  Page<ToolLink> findByTeamIdOrderByCreatedAtDesc(Long teamId, Pageable pageable);
  List<ToolLink> findByTeamIdOrderByCreatedAtDesc(Long teamId);
}
