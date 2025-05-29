package goormthon_group4.backend.domain.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatedProjectDto {
    private Long id;               // 링크 이동용
    private String title;          // 제목 텍스트
    private String imageUrl;       // 프로젝트 포스터 (UI 상단 이미지)
}
