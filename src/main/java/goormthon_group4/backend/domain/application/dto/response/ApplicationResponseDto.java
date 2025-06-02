package goormthon_group4.backend.domain.application.dto.response;

import goormthon_group4.backend.domain.application.entity.Application;
import goormthon_group4.backend.domain.application.entity.ApplicationStatus;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ApplicationResponseDto {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;

    private String imgUrl;
    private String introduce;
    private String purpose;
    private String skillExperience;
    private String strengthsExperience;
    private String additionalInfo;

    private List<String> fileUrls;
    private ApplicationStatus status;

    private Long userId;
    private Long teamId;

    private LocalDateTime submittedAt;


    public static ApplicationResponseDto from(Application application) {
        User user = application.getUser();
        UserInfo userInfo = user.getUserInfo();

        return ApplicationResponseDto.builder()
                .id(application.getId())
                .name(application.getName())
                .email(application.getEmail())
                .phoneNumber(application.getPhoneNumber())
                .imgUrl(userInfo != null ? userInfo.getImgUrl() : null)
                .introduce(application.getIntroduce())
                .purpose(application.getPurpose())
                .skillExperience(application.getSkillExperience())
                .strengthsExperience(application.getStrengthsExperience())
                .additionalInfo(application.getAdditionalInfo())
                .fileUrls(application.getFileUrls())
                .status(application.getStatus())
                .userId(user.getId())
                .teamId(application.getTeam().getId())
                .submittedAt(application.getCreatedAt())  // createdAt도 매핑해주면 좋음
                .build();
    }
}
