package com.clonecod.clone.service;

import com.clonecod.clone.dto.NoticeDto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService {

Page<NoticeDto> getAllNotices(Pageable pageable);    
    NoticeDto getNoticeById(Long id);
    
    // 공지사항 생성 및 수정
    NoticeDto createNotice(NoticeDto noticeDto);
    
    // 공지사항 삭제
    void deleteNotice(Long id);
}
