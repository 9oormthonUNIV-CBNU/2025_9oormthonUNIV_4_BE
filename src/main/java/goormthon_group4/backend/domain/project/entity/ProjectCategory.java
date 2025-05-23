package goormthon_group4.backend.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_category")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProjectCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
