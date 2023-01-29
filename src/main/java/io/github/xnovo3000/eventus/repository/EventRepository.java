package io.github.xnovo3000.eventus.repository;

import io.github.xnovo3000.eventus.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByApprovedIsTrueAndStartIsBeforeAndEndIsAfter(OffsetDateTime firstDate, OffsetDateTime secondDate);
    Page<Event> findAllByApprovedIsTrueAndStartIsAfter(OffsetDateTime firstDate, Pageable pageable);
}