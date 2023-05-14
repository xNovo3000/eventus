package io.github.xnovo3000.eventus.api.repository;

import io.github.xnovo3000.eventus.api.entity.Event;
import io.github.xnovo3000.eventus.api.entity.Subscription;
import io.github.xnovo3000.eventus.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserAndEvent(User user, Event event);
}