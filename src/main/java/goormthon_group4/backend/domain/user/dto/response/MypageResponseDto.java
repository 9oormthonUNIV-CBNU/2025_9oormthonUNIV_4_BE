package goormthon_group4.backend.domain.user.dto.response;

import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MypageResponseDto {
    private String nickname;
    private String university;
    private String major;
    private String introduce;
    private boolean universityAuthenticated;

    public static MypageResponseDto from(User user) {
        UserInfo userInfo = user.getUserInfo();
        return MypageResponseDto.builder()
                .nickname(userInfo.getNickname())
                .university(userInfo.getUniversity())
                .major(userInfo.getMajor())
                .introduce(userInfo.getIntroduce())
                .universityAuthenticated(user.isUniversityAuthenticated())
                .build();
    }
}
