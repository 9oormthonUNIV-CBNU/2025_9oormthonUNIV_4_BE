package goormthon_group4.backend.domain.application.service;

import goormthon_group4.backend.domain.application.dto.request.ApplicationRequestDto;
import goormthon_group4.backend.domain.application.dto.response.ApplicationResponseDto;
import goormthon_group4.backend.domain.application.entity.Application;
import goormthon_group4.backend.domain.application.entity.ApplicationStatus;
import goormthon_group4.backend.domain.application.repository.ApplicationRepository;
import goormthon_group4.backend.domain.s3.service.S3Service;
import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.domain.team.repository.TeamRepository;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.domain.user.repository.UserRepository;
import goormthon_group4.backend.global.auth.CustomUserDetails;
import goormthon_group4.backend.global.common.exception.CustomException;
import goormthon_group4.backend.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final S3Service s3Service;

    @Transactional
    public ApplicationResponseDto submitApplication(ApplicationRequestDto applicationRequestDto,
                                                    CustomUserDetails customUserDetails,
                                                    MultipartFile multipartFile) {
        User user = customUserDetails.getUser();

        // 팀 ID를 기반으로 팀 조회
        Team team = teamRepository.findById(applicationRequestDto.getTeamId())
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        // 포트폴리오 파일 업로드 (nullable)
        String fileurl = null;
        if(multipartFile != null && !multipartFile.isEmpty()) {
            fileurl = s3Service.uploadFile(multipartFile);
        }

        // Application Entity 생성 및 저장
        Application application = Application.builder()
                .status(ApplicationStatus.PENDING)
                .team(team)
                .user(user)
                .name(applicationRequestDto.getName())
                .email(applicationRequestDto.getEmail())
                .phoneNumber(applicationRequestDto.getPhoneNumber())
                .introduce(applicationRequestDto.getIntroduce())
                .purpose(applicationRequestDto.getPurpose())
                .skillExperience(applicationRequestDto.getSkillExperience())
                .strengthsExperience(applicationRequestDto.getStrengthsExperience())
                .additionalInfo(applicationRequestDto.getAdditionalInfo())
                .fileUrl(fileurl)
                .build();

        applicationRepository.save(application);
        return new ApplicationResponseDto(application);
    }
}
