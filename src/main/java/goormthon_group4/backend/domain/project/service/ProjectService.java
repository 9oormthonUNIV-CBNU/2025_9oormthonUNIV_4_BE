package goormthon_group4.backend.domain.project.service;

import goormthon_group4.backend.domain.project.dto.*;
import goormthon_group4.backend.domain.project.entity.Category;
import goormthon_group4.backend.domain.project.entity.Project;
import goormthon_group4.backend.domain.project.entity.ProjectCategory;
import goormthon_group4.backend.domain.project.entity.ProjectStatus;
import goormthon_group4.backend.domain.project.repository.CategoryRepository;
import goormthon_group4.backend.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProjectService {
    private static final int PAGE_SIZE = 8;
    private final ProjectRepository projectRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getProjects(int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(sortBy));
        return projectRepository.findAll(pageable)
                .map(this::mapToResponse);
    }


    @Transactional(readOnly = true)
    public ProjectResponseDto getProjectDetail(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트가 없습니다. id=" + id));

        project.getCategories().forEach(pc -> Hibernate.initialize(pc.getCategory()));

        List<Category> categories = project.getCategories().stream()
                .map(ProjectCategory::getCategory)
                .toList();

        List<Project> similar = projectRepository.findTop6SimilarProjects(categories, project.getId());

        similar.forEach(p -> p.getCategories().forEach(pc -> Hibernate.initialize(pc.getCategory())));

        List<RelatedProjectDto> relatedDtos = similar.stream()
                .map(p -> RelatedProjectDto.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .imageUrl(p.getImageUrl())
                        .build())
                .toList();

        ProjectResponseDto dto = mapToResponse(project);
        dto.setRelatedProjects(relatedDtos);
        return dto;
    }


    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> searchProjects(String keyword, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(sortBy));
        return projectRepository
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> getProjectsByStatus(ProjectStatus status, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(sortBy));
        return projectRepository.findByStatus(status, pageable)
                .map(this::mapToResponse);
    }


    @Transactional
    public ProjectResponseDto createProject(ProjectRequestDto dto) {
        Project project = buildFromDto(dto);
        project.setStatus(determineStatus(dto.getEndAt())); // 자동 설정

        List<ProjectCategory> pcs = dto.getCategories().stream()
                .map(CategoryRequestDto::getTitle)
                .map(String::trim)
                .distinct()
                .map(title -> toProjectCategoryByTitle(title, project))
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
        project.setStartAt(dto.getStartAt());
        project.setEndAt(dto.getEndAt());
        project.setEmail(dto.getEmail());
        project.setFileUrl(dto.getFileUrl());
        project.setImageUrl(dto.getImageUrl());

        project.setStatus(determineStatus(dto.getEndAt()));

        project.getCategories().clear();
        projectRepository.flush();
        List<ProjectCategory> updated = dto.getCategories().stream()
                .map(CategoryRequestDto::getTitle)
                .map(String::trim)
                .distinct()
                .map(title -> toProjectCategoryByTitle(title, project))
                .collect(Collectors.toList());
        project.getCategories().addAll(updated);

        projectRepository.save(project);
        return mapToResponse(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 프로젝트가 없습니다. id=" + id);
        }
        projectRepository.deleteById(id);
    }

    private ProjectCategory toProjectCategoryByTitle(String title, Project project) {
        String clean = title.trim();
        Category cat = categoryRepository.findByTitle(clean)
                .orElseGet(() -> categoryRepository.save(Category.builder()
                        .title(clean)
                        .build()));
        return ProjectCategory.builder()
                .project(project)
                .category(cat)
                .build();
    }

    private String calculateDDay(LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        if (days == 0) return "D-day";
        else if (days > 0) return "D-" + days;
        else return "D+" + Math.abs(days);
    }

    private ProjectStatus determineStatus(LocalDate endAt) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), endAt);
        if (days < 0) return ProjectStatus.Closed;
        else if (days <= 2) return ProjectStatus.Soon;
        else return ProjectStatus.Open;
    }

    private String determineStatusLabel(ProjectStatus status) {
        return switch (status) {
            case Open -> "접수중";
            case Soon -> "마감임박";
            case Closed -> "접수마감";
        };
    }

    private ProjectResponseDto mapToResponse(Project project) {
        List<CategoryResponseDto> cats = project.getCategories().stream()
                .map(pc -> new CategoryResponseDto(
                        pc.getCategory().getId(), pc.getCategory().getTitle()))
                .collect(Collectors.toList());

        return ProjectResponseDto.builder()
                .id(project.getId())
                .companyName(project.getCompanyName())
                .title(project.getTitle())
                .description(project.getDescription())
                .content(project.getContent())
                .status(project.getStatus())
                .statusLabel(determineStatusLabel(project.getStatus()))
                .startAt(project.getStartAt())
                .endAt(project.getEndAt())
                .email(project.getEmail())
                .fileUrl(project.getFileUrl())
                .imageUrl(project.getImageUrl())
                .categories(cats)
                .dDay(calculateDDay(project.getEndAt()))
                .build();
    }

    private Project buildFromDto(ProjectRequestDto dto) {
        return Project.builder()
                .companyName(dto.getCompanyName())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .content(dto.getContent())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .email(dto.getEmail())
                .fileUrl(dto.getFileUrl())
                .imageUrl(dto.getImageUrl())
                .build();
    }
}
