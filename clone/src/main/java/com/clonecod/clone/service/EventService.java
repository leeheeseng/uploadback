package com.clonecod.clone.service;

import com.clonecod.clone.entity.Event;
import com.clonecod.clone.entity.EventDetail;
import com.clonecod.clone.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface EventService {

    
 
    List<Event> getOngoingEvents();

    // 이벤트 목록 조회
Page<Event> getAllEvents(Pageable pageable);
    // 이벤트 ID로 조회
   Event getEventById(Long eventId);
    // 이벤트 저장
    public Event saveEvent(Event event) ;

    // 이벤트 삭제
    public void deleteEvent(Long eventId) ;
    EventDetail updateEventDetail(Long eventId, String eventDetail);
       Event updateEventInfo(Long eventId, String cover, String title, int discountRate, LocalDate startDate, LocalDate endDate);
}
