package com.clonecod.clone.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.clonecod.clone.entity.Event;
import com.clonecod.clone.entity.EventDetail;
import com.clonecod.clone.repository.EventRepository;
import com.clonecod.clone.repository.EventDetailRepository;
import com.clonecod.clone.service.EventService;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventDetailRepository eventDetailRepository;

    // 이벤트 목록 조회
    @Override
    public Page<Event> getAllEvents(Pageable pageable) {
        // 시작일 기준으로 내림차순 정렬하고 페이징 처리
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("startDate").descending());
        return eventRepository.findAll(sortedPageable);  // 랜덤 처리하지 않음
    }

    @Override
    public List<Event> getOngoingEvents() {
        LocalDate today = LocalDate.now();
        return eventRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today);
    }

    @Override
    public Event getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            EventDetail eventDetail = eventDetailRepository.findByEvent_eventId(eventId);
            if (eventDetail != null) {
                event.setEventDetail(eventDetail);
            }
        }
        return event;
    }

    @Override
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

   

    // 이벤트 전체 정보 업데이트: 커버, 제목, 할인율, 날짜
    @Override
    public Event updateEventInfo(Long eventId, String cover, String title, int discountRate, LocalDate startDate, LocalDate endDate) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다. ID: " + eventId));

        event.setCover(cover);
        event.setTitle(title);
        event.setDiscountRate(discountRate);
        event.setStartDate(startDate);
        event.setEndDate(endDate);

        return eventRepository.save(event);
    }

    // 이벤트 상세 설명 업데이트
    @Override
    public EventDetail updateEventDetail(Long eventId, String eventDetail) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다. ID: " + eventId));

        EventDetail detail = eventDetailRepository.findByEvent_eventId(eventId);
        if (detail == null) {
            detail = new EventDetail();
            detail.setEvent(event);
        }

        detail.setEventDetail(eventDetail);
        return eventDetailRepository.save(detail);
    }
}
