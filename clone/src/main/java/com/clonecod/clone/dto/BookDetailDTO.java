package com.clonecod.clone.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDetailDTO {
    private Long cartId;  
    private Long bookId;
    private int subcategoryId;
    private int topCategoryId;
    private Integer price;
    private String writer;
    private String publisher;
    private String bookIndex;
    private String cover;
    private String WritersComment;
    private BigDecimal rating;
    private String title;
    private LocalDate publicationDate;
    private Integer discountRate;
    private Integer quantity;
}