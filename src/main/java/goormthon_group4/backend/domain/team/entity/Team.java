package goormthon_group4.backend.domain.team.entity;

import goormthon_group4.backend.domain.application.entity.Application;
import goormthon_group4.backend.domain.member.Member;
import goormthon_group4.backend.domain.project.entity.Project;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.global.common.base.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;


@Entity
@Table(name = "team")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Team extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Member> members = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
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

  @Setter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  @Setter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "leader_id", nullable = false)
  private User leader;

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Application> applications = new ArrayList<>();

  public void addApplication(Application application) {
    applications.add(application);
//    application.setTeam();
  }

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Notify> notifies = new ArrayList<>();

  public void addNotify(Notify notify) {
    notifies.add(notify);
    //notify.setTeam(this);
  }

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ToolLink> toolLinks = new ArrayList<>();

  public void addToolLink(ToolLink toolLink) {
    toolLinks.add(toolLink);
    //toolLink.setTeam(this);
  }

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Output> outputs = new ArrayList<>();

  public void addOutput(Output output) {
    outputs.add(output);
    // output.setTeam(this);
  }

}
