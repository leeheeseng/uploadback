package com.clonecod.clone.controller;


import com.clonecod.clone.entity.Event;
import com.clonecod.clone.entity.EventDetail;
import com.clonecod.clone.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // 이벤트 전체 조회
   
@GetMapping
public ResponseEntity<Page<Event>> getAllEvents(Pageable pageable) {
    // 시작일 순으로 정렬
    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("startDate").ascending());
    
    // 페이징 처리된 이벤트 목록 반환
    Page<Event> events = eventService.getAllEvents(pageable);
    
    return ResponseEntity.ok(events);
}
@GetMapping("/ONGOING")
public ResponseEntity<List<Event>> getOngoingEvents() {
    List<Event> events = eventService.getOngoingEvents();
    return ResponseEntity.ok(events);
}





    // 이벤트 상세 조회
    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }

    // 이벤트 정보 수정 (커버, 제목, 할인율, 기간)
    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, 
                                             @Valid @RequestBody Event eventDto) {
        Event updatedEvent = eventService.updateEventInfo(
                eventId, 
                eventDto.getCover(), 
                eventDto.getTitle(), 
                eventDto.getDiscountRate(), 
                eventDto.getStartDate(), 
                eventDto.getEndDate()
        );
        return ResponseEntity.ok(updatedEvent);
    }

    // 이벤트 상세 설명 수정
    @PutMapping("/{eventId}/detail")
    public ResponseEntity<EventDetail> updateEventDetail(@PathVariable Long eventId,
                                                         @Valid @RequestBody EventDetail eventDetailDto) {
        EventDetail updatedEventDetail = eventService.updateEventDetail(eventId, eventDetailDto.getEventDetail());
        return ResponseEntity.ok(updatedEventDetail);
    }
}
