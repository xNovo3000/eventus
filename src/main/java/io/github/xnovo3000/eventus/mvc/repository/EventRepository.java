package io.github.xnovo3000.eventus.mvc.repository;

import io.github.xnovo3000.eventus.bean.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndApprovedIsTrue(Long id);
    List<Event> findAllByApprovedIsTrueAndStartIsBeforeAndEndIsAfter(OffsetDateTime firstDate, OffsetDateTime secondDate);
    Page<Event> findAllByApprovedIsTrueAndStartIsAfterOrderByStartAsc(OffsetDateTime firstDate, Pageable pageable);
    Page<Event> findAllByApprovedIsFalseAndStartIsAfterOrderByStartAsc(OffsetDateTime firstDate, Pageable pageable);
}