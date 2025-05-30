package goormthon_group4.backend.domain.member.repository;

import goormthon_group4.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}