package goormthon_group4.backend.domain.user.entity;

import goormthon_group4.backend.domain.application.entity.Application;
import goormthon_group4.backend.domain.member.Member;
import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    @OneToMany(mappedBy = "leader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> leadingTeams = new ArrayList<>();

    public void addTeam(Team team) {
        leadingTeams.add(team);
        team.setLeader(this);
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    // 단방향 매핑 User가 FK 가지고 있음
    @Setter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;

    @Getter
    @Column(name = "is_univ_authenticated", nullable = false)
    private boolean universityAuthenticated = false;

    public void authenticateUniversity() {
        this.universityAuthenticated = true;
    }

    public User(String email, String socialId, Role role, UserInfo userInfo) {
        this.email = email;
        this.socialId = socialId;
        this.role = role;
        this.userInfo = userInfo;
    }



}

