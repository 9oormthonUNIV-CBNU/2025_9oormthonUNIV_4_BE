package goormthon_group4.backend.domain.notify.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NotifySummaryDto {
    private Long id;
    private String title;
    private String createdAt;
    private String lastModified;

    @Builder
    public NotifySummaryDto(Long id, String title, String createdAt, String lastModified) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }
}