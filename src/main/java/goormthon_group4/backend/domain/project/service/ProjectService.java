package goormthon_group4.backend.domain.project.service;

import goormthon_group4.backend.domain.project.dto.ProjectRequestDto;
import goormthon_group4.backend.domain.project.dto.ProjectResponseDto;
import goormthon_group4.backend.domain.project.entity.Project;
import goormthon_group4.backend.domain.project.entity.ProjectCategory;
import goormthon_group4.backend.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectResponseDto createProject(ProjectRequestDto requestDto) {
        Project project = Project.builder()
                .companyName(requestDto.getCompanyName())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .status(requestDto.getStatus())
                .startAt(requestDto.getStartAt())
                .endAt(requestDto.getEndAt())
                .email(requestDto.getEmail())
                .fileUrl(requestDto.getFileUrl())
                .build();

        List<ProjectCategory> categories = requestDto.getCategories().stream()
                .map(title -> ProjectCategory.builder()
                        .title(title)
                        .project(project)
                        .build())
                .collect(Collectors.toList());
        project.setCategories(categories);

        Project saved = projectRepository.save(project);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectResponseDto updateProject(Long id, ProjectRequestDto requestDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트가 존재하지 않습니다. id=" + id));

        project.setCompanyName(requestDto.getCompanyName());
        project.setTitle(requestDto.getTitle());
        project.setContent(requestDto.getContent());
        project.setStatus(requestDto.getStatus());
        project.setStartAt(requestDto.getStartAt());
        project.setEndAt(requestDto.getEndAt());
        project.setEmail(requestDto.getEmail());
        project.setFileUrl(requestDto.getFileUrl());

        // 카테고리 재설정
        project.getCategories().clear();
        List<ProjectCategory> newCategories = requestDto.getCategories().stream()
                .map(title -> ProjectCategory.builder()
                        .title(title)
                        .project(project)
                        .build())
                .collect(Collectors.toList());
        project.getCategories().addAll(newCategories);

        Project updated = projectRepository.save(project);
        return mapToResponse(updated);
    }

    @Transactional
    public void removeProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 프로젝트가 없습니다. id=" + id);
        }
        projectRepository.deleteById(id);
    }

    private ProjectResponseDto mapToResponse(Project project) {
        return ProjectResponseDto.builder()
                .id(project.getId())
                .companyName(project.getCompanyName())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .title(project.getTitle())
                .content(project.getContent())
                .status(project.getStatus())
                .startAt(project.getStartAt())
                .endAt(project.getEndAt())
                .email(project.getEmail())
                .fileUrl(project.getFileUrl())
                .categories(project.getCategories().stream()
                        .map(ProjectCategory::getTitle)
                        .collect(Collectors.toList()))
                .build();
    }
}
