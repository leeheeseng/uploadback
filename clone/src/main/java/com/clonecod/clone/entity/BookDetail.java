package com.clonecod.clone.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bookdetail")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "Subcategory_Id", nullable = false)
    private int subcategoryId;

    @Column(name = "Top_Category_Id", nullable = false)
    private int topCategoryId;

    @Column(name = "Price", nullable = false)
    private Integer price;

    @Column(name = "Writer", nullable = false, length = 100)
    private String writer;

    @Column(name = "Publisher", nullable = false, length = 100)
    private String publisher;

    @Column(name = "Book_Index", columnDefinition = "TEXT")
    private String bookIndex;

    @Column(name = "Cover", columnDefinition = "LONGTEXT", nullable = false)
    private String cover;

    @Column(name = "writers_comment", columnDefinition = "TEXT")
    private String WritersComment;

    @Column(name = "rating", columnDefinition = "DECIMAL(3,2)")
    private BigDecimal rating;

    @Column(name = "Title", nullable = false, length = 255)
    private String title;

    @Column(name = "Publication_date", nullable = false)
    private LocalDate publicationDate;

    @Column(name = "Discount_rate", nullable = false)
    private Integer discountRate = 0;
}