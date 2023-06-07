package io.github.xnovo3000.eventus.api.repository;

import io.github.xnovo3000.eventus.api.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByOrderByStartDesc(Pageable pageable);
    Page<Event> findAllByHoldings_User_UsernameOrderByStartDesc(String username, Pageable pageable);
    List<Event> findAllByApprovedIsTrueAndStartIsBeforeAndEndIsAfter(OffsetDateTime firstDate, OffsetDateTime secondDate);
    Page<Event> findAllByApprovedIsTrueAndStartIsAfterOrderByStartAsc(OffsetDateTime firstDate, Pageable pageable);
    Page<Event> findAllByApprovedIsFalseAndStartIsAfterOrderByStartAsc(OffsetDateTime firstDate, Pageable pageable);
    void deleteAllByApprovedIsFalseAndStartIsBefore(OffsetDateTime now);
}