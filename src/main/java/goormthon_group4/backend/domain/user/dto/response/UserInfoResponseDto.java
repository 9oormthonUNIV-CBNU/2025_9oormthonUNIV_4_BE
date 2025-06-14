package goormthon_group4.backend.domain.user.dto.response;

import goormthon_group4.backend.domain.user.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserInfoResponseDto {
    private Long userId;
    private String nickname;
    private String major;
    private String university;
    private String introduce;
    private String imgUrl;

    public UserInfoResponseDto(UserInfo userInfo) {
        this.userId = userInfo.getUser().getId();
        this.nickname = userInfo.getNickname();
        this.major = userInfo.getMajor();
        this.university = userInfo.getUniversity();
        this.introduce = userInfo.getIntroduce();
        this.imgUrl = userInfo.getImgUrl();
    }
}
