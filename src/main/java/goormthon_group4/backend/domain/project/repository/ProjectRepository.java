package goormthon_group4.backend.domain.project.repository;

import goormthon_group4.backend.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
