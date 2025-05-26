package goormthon_group4.backend.domain.user.entity;

import goormthon_group4.backend.domain.application.entity.Application;
import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.domain.user.entity.Role;
import goormthon_group4.backend.domain.user.entity.UserInfo;
import goormthon_group4.backend.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;

    @OneToMany(mappedBy = "leader", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Team> leadingTeams = new ArrayList<>();

    public void addTeam(Team team) {
        leadingTeams.add(team);
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    public void addApplication(Application application) {
        applications.add(application);
    }
}