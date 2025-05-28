package goormthon_group4.backend.domain.project.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDto {
    private Long id;
    private String title;
}