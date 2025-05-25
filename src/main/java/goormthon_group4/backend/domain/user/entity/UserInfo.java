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
public class UserInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String nickname;

    @Column(length = 50, nullable = false)
    private String major;

    @Column(length = 50, nullable = false)
    private String university;

    @Column(length = 100, nullable = false)
    private String universityEmail;

    @Column(columnDefinition = "TEXT")
    private String introduce;

    @Column(nullable = true)
    private String imgUrl;

    public void update(String nickname, String major, String university, String universityEmail,
                       String introduce, String imgUrl) {
        this.nickname = nickname;
        this.major = major;
        this.university = university;
        this.universityEmail = universityEmail;
        this.introduce = introduce;
        this.imgUrl = imgUrl;
    }

}
