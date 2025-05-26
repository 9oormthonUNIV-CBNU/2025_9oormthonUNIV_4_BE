package goormthon_group4.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRequestDto {
    private String nickname;
    private String major;
    private String university;
    private String introduce;
}
