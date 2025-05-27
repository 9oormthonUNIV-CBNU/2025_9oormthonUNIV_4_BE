package goormthon_group4.backend.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_category", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "category_id"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProjectCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;
}
