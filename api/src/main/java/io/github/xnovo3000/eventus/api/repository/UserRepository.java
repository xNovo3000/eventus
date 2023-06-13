package io.github.xnovo3000.eventus.api.repository;

import io.github.xnovo3000.eventus.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User repository interface. Must not be implemented because Hibernate will.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Get user, if exists, from its email
     *
     * @param email The user's email
     * @return The user, if exists. Empty optional otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Get user, if exists, from its username
     *
     * @param username The user's username
     * @return The user, if exists. Empty optional otherwise
     */
    Optional<User> findByUsername(String username);

    /**
     * Get a page of users listed by username ascending
     *
     * @param pageable The page request
     * @return The page of users
     */
    Page<User> findAllByOrderByUsernameAsc(Pageable pageable);

    /**
     * Get a page of users listed by username ascending and filtered by username
     *
     * @param username The username to filter by
     * @param pageable The page request
     * @return The page of users
     */
    Page<User> findAllByUsernameContainingIgnoreCaseOrderByUsernameAsc(String username, Pageable pageable);

}