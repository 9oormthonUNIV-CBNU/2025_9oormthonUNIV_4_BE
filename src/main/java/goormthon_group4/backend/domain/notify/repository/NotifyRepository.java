package goormthon_group4.backend.domain.notify.repository;

import goormthon_group4.backend.domain.notify.entity.Notify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyRepository extends JpaRepository<Notify, Long> {
    Page<Notify> findByTeamIdOrderByCreatedAtDesc(Long teamId, Pageable pageable);
}

