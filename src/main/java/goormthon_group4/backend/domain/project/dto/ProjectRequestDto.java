package goormthon_group4.backend.domain.project.dto;

import goormthon_group4.backend.domain.project.entity.ProjectStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRequestDto {

    private String companyName;
    private String title;
    private String content;
    private ProjectStatus status;
    private LocalDate startAt;
    private LocalDate endAt;
    private String email;
    private String fileUrl;
    private List<String> categories;
}
