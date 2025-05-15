package com.clonecod.clone.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;  // Jackson을 사용하여 순환 참조 방지

@Entity
@Table(name = "eventdetail")  // 테이블 이름을 eventdetail로 명시
public class EventDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_detail_id")  // 컬럼 이름을 event_detail_id로 명시
    private Long eventDetailId;

    @ManyToOne(fetch = FetchType.LAZY)  // ManyToOne 관계로 Event와 매핑, 지연 로딩(LAZY) 사용
    @JoinColumn(name = "event_id", referencedColumnName = "eventId")  // 외래 키 설정
    @JsonBackReference  // 순환 참조 방지 (Event -> EventDetail -> Event 순환 방지)
    private Event event;

    @Column(name = "event_detail", columnDefinition = "TEXT")  // event_detail 컬럼을 TEXT 타입으로 설정
    private String eventDetail;

    // 기본 생성자
    public EventDetail() {}

    // 생성자
    public EventDetail(Event event, String eventDetail) {
        this.event = event;
        this.eventDetail = eventDetail;
    }

    // Getter, Setter
    public Long getEventDetailId() {
        return eventDetailId;
    }

    public void setEventDetailId(Long eventDetailId) {
        this.eventDetailId = eventDetailId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }
}
