package goormthon_group4.backend.domain.project.controller;

import goormthon_group4.backend.domain.project.dto.ProjectRequestDto;
import goormthon_group4.backend.domain.project.dto.ProjectResponseDto;
import goormthon_group4.backend.domain.project.entity.ProjectStatus;
import goormthon_group4.backend.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<Page<ProjectResponseDto>> listProjects(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) ProjectStatus status,
            @RequestParam(required = false) String keyword
    ) {
        int pageIndex = page <= 1 ? 0 : page - 1;

        Page<ProjectResponseDto> result;
        if (keyword != null && !keyword.isBlank()) {
            result = projectService.searchProjects(keyword, pageIndex);
        } else if (status != null) {
            result = projectService.getProjectsByStatus(status, pageIndex);
        } else {
            result = projectService.getProjects(pageIndex);
        }
        return ResponseEntity.ok(result);
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
