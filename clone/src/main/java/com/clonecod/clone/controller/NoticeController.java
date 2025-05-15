package com.clonecod.clone.controller;

import com.clonecod.clone.dto.NoticeDto;
import com.clonecod.clone.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    // 공지사항 페이징 조회
    @GetMapping
    public Page<NoticeDto> getAllNotices(Pageable pageable) {
        return noticeService.getAllNotices(pageable);
    }

    // 공지사항 상세 조회
    @GetMapping("/{id}")
    public NoticeDto getNoticeById(@PathVariable Long id) {
        return noticeService.getNoticeById(id);
    }

    // 공지사항 생성
    @PostMapping
    public NoticeDto createNotice(@RequestBody NoticeDto noticeDto) {
        return noticeService.createNotice(noticeDto);
    }

    // 공지사항 삭제
    @DeleteMapping("/{id}")
    public void deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
    }
}
