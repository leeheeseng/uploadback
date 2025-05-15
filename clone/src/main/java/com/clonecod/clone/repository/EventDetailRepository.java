package com.clonecod.clone.repository;

import com.clonecod.clone.entity.EventDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface EventDetailRepository extends JpaRepository<EventDetail, Long> {
       EventDetail findByEvent_eventId(Long eventId);
}
