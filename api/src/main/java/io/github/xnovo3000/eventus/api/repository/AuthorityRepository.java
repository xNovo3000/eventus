package io.github.xnovo3000.eventus.api.repository;

import io.github.xnovo3000.eventus.api.entity.Authority;
import io.github.xnovo3000.eventus.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Authority repository interface. Must not be implemented because Hibernate will.
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    /**
     * Delete alla authorities for a specific user
     *
     * @param user The user
     */
    void deleteAllByUser(User user);

}