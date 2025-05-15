package com.clonecod.clone.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookListDTO {
    private Long bookId;
    private String title;
    private String author;
    private int price;
    private String imageUrl;
    private String publisher;
    // 기타 목록에 필요한 필드들
}