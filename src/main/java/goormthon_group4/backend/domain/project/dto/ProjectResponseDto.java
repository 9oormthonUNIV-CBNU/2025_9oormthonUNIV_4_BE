package goormthon_group4.backend.domain.project.dto;

import goormthon_group4.backend.domain.project.entity.ProjectStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponseDto {

    private Long id;
    private String companyName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String title;
    private String content;
    private ProjectStatus status;
    private LocalDate startAt;
    private LocalDate endAt;
    private String email;
    private String fileUrl;
    private List<String> categories;
}
