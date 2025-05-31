package goormthon_group4.backend.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberResponseDto {
    private Long userId;
    private String username;
    private String imgUrl;
    private boolean isLeader;
    private int joinedDaysAgo;
    private LocalDateTime kickedAt;

    @Builder
    public MemberResponseDto(Long userId, String username, String imgUrl,
                             boolean isLeader, int joinedDaysAgo,LocalDateTime kickedAt) {
        this.userId = userId;
        this.username = username;
        this.imgUrl = imgUrl;
        this.isLeader = isLeader;
        this.joinedDaysAgo = joinedDaysAgo;
        this.kickedAt = kickedAt;
    }
}
