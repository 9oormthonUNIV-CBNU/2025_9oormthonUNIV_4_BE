package goormthon_group4.backend.domain.user.dto.response;

import goormthon_group4.backend.domain.application.entity.Application;
import goormthon_group4.backend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MypageFullResponseDto {
    private MypageResponseDto userInfo;
    private List<MypageTeamDto> applications;

    public static MypageFullResponseDto form(User user, List<Application> applications) {
        List<MypageTeamDto> teamDtos = applications.stream()
                .map(MypageTeamDto::from)
                .toList();

        return new MypageFullResponseDto(
                MypageResponseDto.from(user),
                teamDtos
        );
    }
}
