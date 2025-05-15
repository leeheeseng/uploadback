package com.clonecod.clone.dto;

import com.clonecod.clone.entity.Notice;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NoticeDto {

    private Long id;
    private String title;
    private String author;
    private LocalDateTime date;
    private int views;
    private String content;

    public static NoticeDto fromEntity(Notice notice) {
        NoticeDto dto = new NoticeDto();
        dto.setId(notice.getId());
        dto.setTitle(notice.getTitle());
        dto.setAuthor(notice.getAuthor());
        dto.setDate(notice.getDate());
        dto.setViews(notice.getViews());
        dto.setContent(notice.getContent());
        return dto;
    }
}
