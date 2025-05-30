package goormthon_group4.backend.domain.user.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoRequestDto {
    private String nickname;
    private String major;
    private String university;
    private String introduce;
    private String imgUrl;
}
