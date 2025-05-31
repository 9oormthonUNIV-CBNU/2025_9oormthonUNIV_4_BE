package goormthon_group4.backend.domain.project.repository;

import goormthon_group4.backend.domain.project.entity.Category;
import goormthon_group4.backend.domain.project.entity.Project;
import goormthon_group4.backend.domain.project.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAll(Pageable pageable);

    Page<Project> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String titleKeyword,
            String contentKeyword,
            Pageable pageable
    );

    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);
    @Query("""
    SELECT DISTINCT p FROM Project p
    JOIN p.categories pc
    WHERE pc.category IN :categories AND p.id <> :projectId
    ORDER BY p.createdAt DESC
""")
    List<Project> findTop6SimilarProjects(@Param("categories") List<Category> categories,
                                          @Param("projectId") Long projectId);
}