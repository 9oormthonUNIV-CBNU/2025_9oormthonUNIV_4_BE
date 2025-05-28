package goormthon_group4.backend.domain.project.repository;

import goormthon_group4.backend.domain.project.entity.Project;
import goormthon_group4.backend.domain.project.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAll(Pageable pageable);

    Page<Project> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String titleKeyword,
            String contentKeyword,
            Pageable pageable
    );

    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);

}