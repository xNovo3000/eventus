package io.github.xnovo3000.eventus.api.repository;

import io.github.xnovo3000.eventus.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Page<User> findAllByOrderByUsernameAsc(Pageable pageable);
    Page<User> findAllByUsernameContainingIgnoreCaseOrderByUsernameAsc(String username, Pageable pageable);
}