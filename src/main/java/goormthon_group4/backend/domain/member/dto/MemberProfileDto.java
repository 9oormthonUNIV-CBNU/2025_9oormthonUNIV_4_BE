package goormthon_group4.backend.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberProfileDto {
    private String nickname;
    private String imgUrl;
    private String university;
    private String major;
    private String introduce;
}
