package io.github.xnovo3000.eventus.api.repository;

import io.github.xnovo3000.eventus.api.entity.Event;
import io.github.xnovo3000.eventus.api.entity.Subscription;
import io.github.xnovo3000.eventus.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Subscription repository interface. Must not be implemented because Hibernate will.
 */
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /**
     * Find the subscription to the event from its user and event, if exists
     *
     * @param user The user
     * @param event The event
     * @return The subscription, if exists. Empty optional otherwise
     */
    Optional<Subscription> findByUserAndEvent(User user, Event event);

}