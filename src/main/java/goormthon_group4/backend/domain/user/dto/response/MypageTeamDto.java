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
        private String statusLabel;

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

        private static String getStatusLabel(ApplicationStatus status) {
            return switch (status) {
                case PENDING -> "참여 신청중";
                case ACCEPT -> "진행 중";
                case REJECTED -> "참여 취소됨";
            };
        }

    }
