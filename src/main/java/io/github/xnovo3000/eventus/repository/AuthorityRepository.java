package io.github.xnovo3000.eventus.repository;

import io.github.xnovo3000.eventus.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {}