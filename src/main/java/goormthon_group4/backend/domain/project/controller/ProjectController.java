package goormthon_group4.backend.domain.project.controller;

import goormthon_group4.backend.domain.project.dto.ProjectRequestDto;
import goormthon_group4.backend.domain.project.dto.ProjectResponseDto;
import goormthon_group4.backend.domain.project.entity.ProjectStatus;
import goormthon_group4.backend.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<Page<ProjectResponseDto>> listProjects(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) ProjectStatus status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "startAt") String sortBy // 정렬 기준만 받음
    ) {
        int pageIndex = Math.max(0, page - 1);
        Page<ProjectResponseDto> result;

        if (keyword != null && !keyword.isBlank()) {
            result = projectService.searchProjects(keyword, pageIndex, sortBy);
        } else if (status != null) {
            result = projectService.getProjectsByStatus(status, pageIndex, sortBy);
        } else {
            result = projectService.getProjects(pageIndex, sortBy);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectDetail(@PathVariable Long id) {
        ProjectResponseDto dto = projectService.getProjectDetail(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(
            @RequestBody ProjectRequestDto requestDto
    ) {
        ProjectResponseDto created = projectService.createProject(requestDto);
        return ResponseEntity.ok(created);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectRequestDto requestDto
    ) {
        ProjectResponseDto updated = projectService.updateProject(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
