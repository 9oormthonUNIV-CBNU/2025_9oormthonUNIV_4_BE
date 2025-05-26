package goormthon_group4.backend.domain.user.dto;

import goormthon_group4.backend.domain.user.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {
    private String nickname;
    private String major;
    private String university;
    private String introduce;

    public UserInfoResponseDto(UserInfo userInfo) {
        this.nickname = userInfo.getNickname();
        this.major = userInfo.getMajor();
        this.university = userInfo.getUniversity();
        this.introduce = userInfo.getIntroduce();
    }
}
