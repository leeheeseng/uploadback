package com.clonecod.clone.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clonecod.clone.common.JwtUtil;
import com.clonecod.clone.dto.LoginRequestDto;
import com.clonecod.clone.dto.SignupRequestDto;
import com.clonecod.clone.entity.Member;
import com.clonecod.clone.dto.MemberUpdateDto;  // MemberUpdateDto 추가
import com.clonecod.clone.service.MemberService;

@RestController
@RequestMapping("/api/member")
@CrossOrigin(origins = "http://localhost:3000")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;  // JwtUtil 주입

    // Constructor에서 MemberService와 JwtUtil을 주입받음
    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;  // JwtUtil 초기화
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupDto) {
        boolean isSuccess = memberService.signup(signupDto);
        if (isSuccess) {
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공!");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 아이디입니다.");
        }
    }

    // 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
        // 로그인 시 login_id를 바탕으로 memberId를 가져옴
        Long memberId = memberService.login(dto);

        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "message", "로그인 정보가 잘못되었습니다."
            ));
        }

        // JWT 토큰 생성
        String token = jwtUtil.generateToken(dto.getUserId()); // 로그인 아이디를 토큰에 포함시킬 수 있음

        // 토큰과 함께 memberId를 별도로 반환
        return ResponseEntity.ok(Map.of(
            "token", token,
            "memberId", memberId,  // member_id 반환
            "message", "로그인 성공"
        ));
    }

    // 아이디 중복 체크
    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkLoginIdDuplicate(@RequestParam String loginId) {
        boolean isDuplicate = memberService.isLoginIdDuplicate(loginId);
        return ResponseEntity.ok(isDuplicate);
    }

    // 사용자 정보 조회
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestParam Long memberId) {
        try {
            Map<String, Object> userInfo = memberService.getUserInfo(memberId);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "message", "사용자 정보를 찾을 수 없습니다."
            ));
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> updateUserInfo(@RequestBody MemberUpdateDto updateDto) {
        try {
            Long memberId = updateDto.getMemberId();  // 클라이언트에서 받은 memberId
    
            // 회원 정보 수정 서비스 호출
            Member updatedMember = memberService.updateMemberInfo(memberId, updateDto);
    
            return ResponseEntity.ok(Map.of("message", "회원정보 수정 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "서버 오류"));
        }
    }
}
