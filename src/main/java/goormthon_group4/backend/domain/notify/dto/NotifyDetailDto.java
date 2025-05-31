package goormthon_group4.backend.domain.notify.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NotifyDetailDto {
    private String title;
    private String content;
    private String createdAt;

    @Builder
    public NotifyDetailDto(String title, String content, String createdAt) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
