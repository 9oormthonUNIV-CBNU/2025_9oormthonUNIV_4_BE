package goormthon_group4.backend.domain.application.entity;

import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Application extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private ApplicationStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id", nullable = false)
  private Team team;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String introduce;

  @Column(nullable = false)
  private String purpose;

  @Column(nullable = false)
  private String skillExperience;

  @Column(nullable = false)
  private String strengthsExperience;

//  @Column(length = 2048, nullable = true)
//  private String fileUrl; // nullable

  @ElementCollection
  @CollectionTable(name = "application_file_urls", joinColumns = @JoinColumn(name = "application_id"))
  @Column(name = "file_url", length = 2048) // ✅ 길이 늘리기
  private List<String> fileUrls = new ArrayList<>();

  @Column(nullable = true)
  private String additionalInfo; // nullable

  // 지원서 상태 변경
  public void changeStatus(ApplicationStatus status) {
    this.status = status;
  }
}
