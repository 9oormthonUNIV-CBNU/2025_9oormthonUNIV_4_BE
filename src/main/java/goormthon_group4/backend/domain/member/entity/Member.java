package goormthon_group4.backend.domain.member.entity;

import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false)
    private boolean isLeader;

    @Column(name = "kicked_at")
    private LocalDateTime kickedAt;

    public void kickOut() {
        this.kickedAt = LocalDateTime.now();
    }

}