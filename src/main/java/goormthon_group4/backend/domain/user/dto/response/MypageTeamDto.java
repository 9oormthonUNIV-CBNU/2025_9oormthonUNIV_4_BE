    package goormthon_group4.backend.domain.user.dto.response;

    import goormthon_group4.backend.domain.application.entity.Application;
    import goormthon_group4.backend.domain.application.entity.ApplicationStatus;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.NoArgsConstructor;

    import java.time.LocalDate;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class MypageTeamDto {
        private Long applicationId;
        private String teamName;
        private String projectTitle;
        private String leaderName;
        private LocalDate createdAt;
        private ApplicationStatus status;

        public static MypageTeamDto from(Application application) {
            return MypageTeamDto.builder()
                    .applicationId(application.getId())
                    .teamName(application.getTeam().getTitle())
                    .projectTitle(application.getTeam().getProject().getTitle())
                    .leaderName(application.getTeam().getLeader().getUserInfo().getNickname())
                    .createdAt(application.getCreatedAt().toLocalDate())
                    .status(application.getStatus())
                    .build();
        }

    }
