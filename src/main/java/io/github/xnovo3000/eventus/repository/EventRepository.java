package io.github.xnovo3000.eventus.repository;

import io.github.xnovo3000.eventus.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {}