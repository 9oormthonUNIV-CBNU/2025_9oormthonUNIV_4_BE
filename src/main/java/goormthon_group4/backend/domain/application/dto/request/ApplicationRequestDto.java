package goormthon_group4.backend.domain.application.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class ApplicationRequestDto {
    private Long teamId;

    private String name;
    private String email;
    private String phoneNumber;

    private String introduce;
    private String purpose;
    private String skillExperience;
    private String strengthsExperience;
    private String additionalInfo;

    private MultipartFile file; // 첨부파일

}
