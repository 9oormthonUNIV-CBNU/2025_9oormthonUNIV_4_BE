package goormthon_group4.backend.domain.application.dto.response;

import goormthon_group4.backend.domain.application.entity.Application;
import goormthon_group4.backend.domain.application.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ApplicationResponseDto {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;

    private String introduce;
    private String purpose;
    private String skillExperience;
    private String strengthsExperience;

    private String fileUrl;
    private ApplicationStatus status;

    private Long userId;
    private Long teamId;

    public static ApplicationResponseDto from(Application application) {
        return ApplicationResponseDto.builder()
                .id(application.getId())
                .name(application.getName())
                .email(application.getEmail())
                .phoneNumber(application.getPhoneNumber())
                .introduce(application.getIntroduce())
                .purpose(application.getPurpose())
                .skillExperience(application.getSkillExperience())
                .strengthsExperience(application.getStrengthsExperience())
                .fileUrl(application.getFileUrl())
                .userId(application.getUser().getId())
                .teamId(application.getTeam().getId())
                .build();
    }
}
