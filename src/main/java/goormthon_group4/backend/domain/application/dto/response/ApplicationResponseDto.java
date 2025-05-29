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

    @Builder
    public ApplicationResponseDto(Application application) {
        this.id = application.getId();
        this.name = application.getName();
        this.email = application.getEmail();
        this.phoneNumber = application.getPhoneNumber();
        this.introduce = application.getIntroduce();
        this.purpose = application.getPurpose();
        this.skillExperience = application.getSkillExperience();
        this.strengthsExperience = application.getStrengthsExperience();
    }
}
