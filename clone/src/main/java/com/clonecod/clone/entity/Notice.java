package com.clonecod.clone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notices")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    private String title;

    private String author;

    private LocalDateTime date;

    private int views;

    @Column(columnDefinition = "TEXT")
    private String content;

    // 생성자
    public Notice(String title, String author, LocalDateTime date, int views, String content) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.views = views;
        this.content = content;
    }
}
