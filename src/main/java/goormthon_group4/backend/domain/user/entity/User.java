package goormthon_group4.backend.domain.user.entity;

import goormthon_group4.backend.global.common.base.BaseEntity;
import jakarta.persistence.*;
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

    private String socialId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

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

