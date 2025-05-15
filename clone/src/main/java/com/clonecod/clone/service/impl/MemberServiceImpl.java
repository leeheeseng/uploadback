package com.clonecod.clone.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clonecod.clone.common.JwtUtil;
import com.clonecod.clone.dto.LoginRequestDto;
import com.clonecod.clone.dto.SignupRequestDto;
import com.clonecod.clone.dto.MemberUpdateDto; // 회원 정보 수정 DTO
import com.clonecod.clone.entity.Member;
import com.clonecod.clone.repository.MemberRepository;
import com.clonecod.clone.service.MemberService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // ✅ 주입 받음

    // 🔧 생성자에 JwtUtil 추가
    public MemberServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean signup(SignupRequestDto dto) {
        if (memberRepository.existsByLoginId(dto.getUserId())) {
            return false;
        }

        Member member = new Member();
        member.setLoginId(dto.getUserId());
        member.setPassword(passwordEncoder.encode(dto.getPassword()));
        member.setMemberName(dto.getName());
        member.setBirthday(dto.getBirthDate());
        member.setGender(dto.getGender());
        member.setEmail(dto.getEmail());
        member.setTel(dto.getPhone());

        memberRepository.save(member);
        return true;
    }

    @Override
    public Long login(LoginRequestDto dto) {
        // 로그인 아이디와 비밀번호를 사용하여 회원 정보를 찾기
        Optional<Member> memberOpt = memberRepository.findByLoginId(dto.getUserId());

        // 회원이 존재하고 비밀번호가 일치하는지 체크
        if (memberOpt.isPresent() && passwordEncoder.matches(dto.getPassword(), memberOpt.get().getPassword())) {
            // 로그인 성공 시 memberId 반환
            return memberOpt.get().getMemberId();  // memberId를 반환
        } else {
            return null;  // 로그인 실패 시 null 반환
        }
    }

    @Override
    public boolean isLoginIdDuplicate(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    @Override
    public Map<String, Object> getUserInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Map<String, Object> result = new HashMap<>();
        result.put("name", member.getMemberName());
        result.put("email", member.getEmail());
        result.put("loginId", member.getLoginId());
        result.put("birthday", member.getBirthday());
        result.put("gender", member.getGender());
        result.put("tel", member.getTel());
        return result;
    }

    // 회원 정보 수정 (비밀번호 포함)
    @Override
    public Member updateMemberInfo(Long memberId, MemberUpdateDto updatedMember) {
        Member existingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        // 비밀번호가 변경되었는지 확인
        if (updatedMember.getPassword() != null && !updatedMember.getPassword().isEmpty()) {
            existingMember.setPassword(passwordEncoder.encode(updatedMember.getPassword()));
        }

        // 이름, 생년월일, 성별, 이메일, 전화번호 수정
        existingMember.setMemberName(updatedMember.getName());
        existingMember.setBirthday(updatedMember.getBirthDate());
        existingMember.setGender(updatedMember.getGender());
        existingMember.setEmail(updatedMember.getEmail());
        existingMember.setTel(updatedMember.getPhone());

        return memberRepository.save(existingMember); // 수정된 회원 정보를 저장
    }
}
