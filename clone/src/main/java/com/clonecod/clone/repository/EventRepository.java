package com.clonecod.clone.repository;

import com.clonecod.clone.entity.Event;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {
     Event findFirstByCoverIsNullOrCover(String emptyCover);

    @Query("SELECT e FROM Event e WHERE e.eventId > :eventId AND (e.cover IS NULL OR e.cover = '') ORDER BY e.eventId ASC")
    Event findNextByIdAndCoverIsNullOrEmpty(@Param("eventId") Long eventId);

    Event findFirstByOrderByEventIdAsc();

    Event findFirstByEventIdGreaterThanOrderByEventIdAsc(Long eventId);

    List<Event> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate start, LocalDate end);
}
