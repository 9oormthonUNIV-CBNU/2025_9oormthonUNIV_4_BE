package goormthon_group4.backend.domain.team.entity;

import goormthon_group4.backend.domain.application.entity.Application;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.global.common.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Team extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private TeamStatus status;

  @Column(nullable = false)
  private Integer maxUserCount;

  @Column(columnDefinition = "TIMESTAMP(6)", nullable = false)
  private LocalDateTime startAt;

  @Column(columnDefinition = "TIMESTAMP(6)", nullable = false)
  private LocalDateTime endAt;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  // 프로젝트랑 one to one
  @Column(nullable = true)
  private String fileUrl;

  @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private TeamInfo teamInfo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "leader_id", nullable = false)
  private User leader;

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Application> applications = new ArrayList<>();

  public void addApplication(Application application) {
    applications.add(application);
//    application.setTeam();
  }

}
