package goormthon_group4.backend.domain.user.entity;

import goormthon_group4.backend.domain.application.entity.Application;
import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.global.common.base.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    private String socialId;

<<<<<<< HEAD
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_info_id", nullable = true)
    private UserInfo userInfo;
=======
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
>>>>>>> 5648c99 (feat : 구글 oauth 로그인 구현, JWT 인증 완료)

    @OneToMany(mappedBy = "leader", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Team> leadingTeams = new ArrayList<>();

    public void addTeam(Team team) {
        leadingTeams.add(team);
//        team.setUser(this);
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    public void addApplication(Application application) {
        applications.add(application);
//        application.setUser(this);

    }
}

