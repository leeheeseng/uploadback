package com.clonecod.clone.service.impl;

import com.clonecod.clone.dto.NoticeDto;
import com.clonecod.clone.entity.Notice;
import com.clonecod.clone.repository.NoticeRepository;
import com.clonecod.clone.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Autowired
    public NoticeServiceImpl(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @Override
    public Page<NoticeDto> getAllNotices(Pageable pageable) {
        return noticeRepository.findAll(pageable)
                               .map(NoticeDto::fromEntity);
    }

    @Override
    public NoticeDto getNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
        return NoticeDto.fromEntity(notice);
    }

    @Override
    public NoticeDto createNotice(NoticeDto noticeDto) {
        Notice notice = new Notice(
                noticeDto.getTitle(),
                noticeDto.getAuthor(),
                noticeDto.getDate(),
                noticeDto.getViews(),
                noticeDto.getContent()
        );
        Notice savedNotice = noticeRepository.save(notice);
        return NoticeDto.fromEntity(savedNotice);
    }

    @Override
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }
}
