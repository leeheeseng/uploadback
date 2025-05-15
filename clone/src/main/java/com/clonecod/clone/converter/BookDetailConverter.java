package com.clonecod.clone.converter;

import com.clonecod.clone.dto.BookDetailDTO;
import com.clonecod.clone.entity.BookDetail;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BookDetailConverter {
    public static BookDetailDTO toDto(BookDetail entity) {
        return BookDetailDTO.builder()
                .bookId(entity.getBookId())
                .subcategoryId(entity.getSubcategoryId())
                .topCategoryId(entity.getTopCategoryId())
                .price(entity.getPrice())
                .writer(entity.getWriter())
                .publisher(entity.getPublisher())
                .bookIndex(entity.getBookIndex())
                .cover(entity.getCover())
                .WritersComment(entity.getWritersComment())
                .rating(entity.getRating())
                .title(entity.getTitle())
                .publicationDate(entity.getPublicationDate())
                .discountRate(entity.getDiscountRate())
                .build();
    }

    public static BookDetail toEntity(BookDetailDTO dto) {
        return BookDetail.builder()
                .bookId(dto.getBookId())
                .subcategoryId(dto.getSubcategoryId())
                .topCategoryId(dto.getTopCategoryId())
                .price(dto.getPrice())
                .writer(dto.getWriter())
                .publisher(dto.getPublisher())
                .bookIndex(dto.getBookIndex())
                .cover(dto.getCover())
                .WritersComment(dto.getWritersComment())
                .rating(dto.getRating())
                .title(dto.getTitle())
                .publicationDate(dto.getPublicationDate())
                .discountRate(dto.getDiscountRate())
                .build();
    }
}