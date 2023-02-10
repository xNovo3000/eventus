package io.github.xnovo3000.eventus.repository;

import io.github.xnovo3000.eventus.entity.Event;
import io.github.xnovo3000.eventus.entity.Participation;
import io.github.xnovo3000.eventus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    Optional<Participation> findByUserAndEvent(User user, Event event);
}