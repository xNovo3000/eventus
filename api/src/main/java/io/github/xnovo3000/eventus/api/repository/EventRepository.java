package io.github.xnovo3000.eventus.api.repository;

import io.github.xnovo3000.eventus.api.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Event repository interface. Must not be implemented because Hibernate will.
 */
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Get a page of events ordered by start date descending
     *
     * @param pageable The page request
     * @return The page of events
     */
    Page<Event> findAllByOrderByStartDesc(Pageable pageable);

    /**
     * Get a page of events that the user will attend ordered by start date descending
     *
     * @param username The user's username
     * @param pageable The page request
     * @return The page of events
     */
    Page<Event> findAllByHoldings_User_UsernameOrderByStartDesc(String username, Pageable pageable);

    /**
     * Get a page of ongoing events
     *
     * @param firstDate The date before start
     * @param secondDate The date after end
     * @return The list of events
     */
    List<Event> findAllByApprovedIsTrueAndStartIsBeforeAndEndIsAfter(OffsetDateTime firstDate, OffsetDateTime secondDate);

    /**
     * Get a page of future events
     *
     * @param firstDate The date to start from
     * @param pageable The page request
     * @return The page of events
     */
    Page<Event> findAllByApprovedIsTrueAndStartIsAfterOrderByStartAsc(OffsetDateTime firstDate, Pageable pageable);

    /**
     * Get a page of proposed events
     *
     * @param firstDate The date to start from
     * @param pageable The page request
     * @return The page of events
     */
    Page<Event> findAllByApprovedIsFalseAndStartIsAfterOrderByStartAsc(OffsetDateTime firstDate, Pageable pageable);

    /**
     * Delete all expired, unapproved events
     *
     * @param now Current date and time
     */
    void deleteAllByApprovedIsFalseAndStartIsBefore(OffsetDateTime now);

}