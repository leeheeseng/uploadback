package com.clonecod.clone.service;

import java.util.Map;

import com.clonecod.clone.dto.LoginRequestDto;
import com.clonecod.clone.dto.MemberUpdateDto;
import com.clonecod.clone.dto.SignupRequestDto;
import com.clonecod.clone.entity.Member;

public interface MemberService {
    boolean signup(SignupRequestDto dto);
    Long login(LoginRequestDto dto); // 변경됨
    boolean isLoginIdDuplicate(String loginId);
    Map<String, Object> getUserInfo(Long memberId);
    Member updateMemberInfo(Long memberId, MemberUpdateDto updatedMember);
}
