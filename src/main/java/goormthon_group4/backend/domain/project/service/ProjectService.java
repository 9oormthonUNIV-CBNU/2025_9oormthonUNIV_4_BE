package goormthon_group4.backend.domain.project.service;

import goormthon_group4.backend.domain.project.dto.CategoryRequestDto;
import goormthon_group4.backend.domain.project.dto.CategoryResponseDto;
import goormthon_group4.backend.domain.project.dto.ProjectRequestDto;
import goormthon_group4.backend.domain.project.dto.ProjectResponseDto;
import goormthon_group4.backend.domain.project.entity.Category;
import goormthon_group4.backend.domain.project.entity.Project;
import goormthon_group4.backend.domain.project.entity.ProjectCategory;
import goormthon_group4.backend.domain.project.entity.ProjectStatus;
import goormthon_group4.backend.domain.project.repository.CategoryRepository;
import goormthon_group4.backend.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProjectService {
    private static final int PAGE_SIZE = 8;
    private final ProjectRepository projectRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getProjects(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        return projectRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> searchProjects(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        return projectRepository
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getProjectsByStatus(ProjectStatus status, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        return projectRepository.findByStatus(status, pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    public ProjectResponseDto createProject(ProjectRequestDto dto) {
        Project project = buildFromDto(dto);
        List<ProjectCategory> pcs = dto.getCategories().stream()
                .map(cr -> toProjectCategory(cr, project))
                .collect(Collectors.toList());
        project.setCategories(pcs);
        return mapToResponse(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponseDto updateProject(Long id, ProjectRequestDto dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트가 없습니다. id=" + id));

        project.setCompanyName(dto.getCompanyName());
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setContent(dto.getContent());
        project.setStatus(dto.getStatus());
        project.setStartAt(dto.getStartAt());
        project.setEndAt(dto.getEndAt());
        project.setEmail(dto.getEmail());
        project.setFileUrl(dto.getFileUrl());
        project.setImageUrl(dto.getImageUrl());

        // 카테고리 재설정
        project.getCategories().clear();
        List<ProjectCategory> updated = dto.getCategories().stream()
                .map(cr -> toProjectCategory(cr, project))
                .collect(Collectors.toList());
        project.getCategories().addAll(updated);

        return mapToResponse(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 프로젝트가 없습니다. id=" + id);
        }
        projectRepository.deleteById(id);
    }

    private ProjectCategory toProjectCategory(CategoryRequestDto cr, Project project) {
        Category category;
        if (cr.getId() != null) {
            category = categoryRepository.findById(cr.getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 id=" + cr.getId()));
        } else {
            category = categoryRepository.findByTitle(cr.getTitle())
                    .orElseGet(() -> categoryRepository.save(
                            Category.builder().title(cr.getTitle()).build()
                    ));
        }
        return ProjectCategory.builder()
                .project(project)
                .category(category)
                .build();
    }

    private ProjectResponseDto mapToResponse(Project project) {
        List<CategoryResponseDto> cats = project.getCategories().stream()
                .map(pc -> new CategoryResponseDto(
                        pc.getCategory().getId(), pc.getCategory().getTitle()))
                .collect(Collectors.toList());

        return ProjectResponseDto.builder()
                .id(project.getId())
                .companyName(project.getCompanyName())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .title(project.getTitle())
                .description(project.getDescription())
                .content(project.getContent())
                .status(project.getStatus())
                .startAt(project.getStartAt())
                .endAt(project.getEndAt())
                .email(project.getEmail())
                .fileUrl(project.getFileUrl())
                .imageUrl(project.getImageUrl())
                .categories(cats)
                .build();
    }

    private Project buildFromDto(ProjectRequestDto dto) {
        return Project.builder()
                .companyName(dto.getCompanyName())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .content(dto.getContent())
                .status(dto.getStatus())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .email(dto.getEmail())
                .fileUrl(dto.getFileUrl())
                .imageUrl(dto.getImageUrl())
                .build();
    }
}
