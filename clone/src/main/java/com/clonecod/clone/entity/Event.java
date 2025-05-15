package com.clonecod.clone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Getter
@Setter
@AllArgsConstructor // 모든 필드를 초기화하는 생성자 생성
@NoArgsConstructor  // 기본 생성자 생성
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String title;
    private int discountRate;
    private Long bookId;
    private String cover;
    private String bigCover;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    @JsonManagedReference // 순환 참조 방지
    private EventDetail eventDetail;

    // Getter, Setter...
}

