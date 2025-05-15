package com.clonecod.clone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clonecod.clone.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByLoginId(String login_Id);
    Optional<Member> findByLoginId(String loginId);
    
}
