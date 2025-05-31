package goormthon_group4.backend.domain.notify.service;

import goormthon_group4.backend.domain.notify.dto.NotifyDetailDto;
import goormthon_group4.backend.domain.notify.dto.NotifyRequestDto;
import goormthon_group4.backend.domain.notify.dto.NotifySummaryDto;
import goormthon_group4.backend.domain.notify.dto.NotifyUpdateRequestDto;
import goormthon_group4.backend.domain.notify.entity.Notify;
import goormthon_group4.backend.domain.notify.repository.NotifyRepository;
import goormthon_group4.backend.domain.team.entity.Team;
import goormthon_group4.backend.domain.team.repository.TeamRepository;
import goormthon_group4.backend.domain.user.entity.User;
import goormthon_group4.backend.global.common.exception.CustomException;
import goormthon_group4.backend.global.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class NotifyService {

    private final NotifyRepository notifyRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public void create(Long teamId, User user, NotifyRequestDto dto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "팀을 찾을 수 없습니다."));

        if (!team.getLeader().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN, "팀장만 공지사항을 작성할 수 있습니다.");
        }

        Notify notify = Notify.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .team(team)
                .user(user)
                .build();

        notifyRepository.save(notify);
    }

    @Transactional
    public void update(Long notifyId, Long userId, NotifyUpdateRequestDto dto) {
        Notify notify = notifyRepository.findById(notifyId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFY_NOT_FOUND));

        // 팀의 리더인지 확인
        Team team = notify.getTeam();
        Long leaderId = team.getLeader().getId();
        if (!leaderId.equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN, "공지사항 수정 권한이 없습니다."); // 403
        }

        // 공지사항 내용 업데이트
        notify.setTitle(dto.getTitle());
        notify.setContent(dto.getContent());
    }

    @Transactional
    public void delete(Long teamId, Long notifyId, Long userId) {
        Notify notify = notifyRepository.findById(notifyId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFY_NOT_FOUND));

        Team team = notify.getTeam();
        if (!team.getId().equals(teamId)) {
            throw new CustomException(ErrorCode.FORBIDDEN, "팀 정보가 일치하지 않습니다.");
        }

        if (!team.getLeader().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN, "삭제 권한이 없습니다.");
        }

        notifyRepository.delete(notify);
    }

    @Transactional(readOnly = true)
    public NotifyDetailDto getDetail(Long notifyId) {
        Notify notify = notifyRepository.findById(notifyId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "공지사항을 찾을 수 없습니다."));

        return NotifyDetailDto.builder()
                .title(notify.getTitle())
                .content(notify.getContent())
                .createdAt(notify.getCreatedAt().toString())  // 필요하면 포맷 지정 가능
                .build();
    }


    @Transactional(readOnly = true)
    public Page<NotifySummaryDto> getNoticesByTeam(Long teamId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Notify> notifyPage = notifyRepository.findByTeamIdOrderByCreatedAtDesc(teamId, pageable);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return notifyPage.map(notify -> NotifySummaryDto.builder()
                .id(notify.getId())
                .title(notify.getTitle())
                .createdAt(notify.getCreatedAt().format(formatter))
                .lastModified(notify.getUpdatedAt().format(formatter))
                .build());
    }

}
