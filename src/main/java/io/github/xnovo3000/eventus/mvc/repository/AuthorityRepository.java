package io.github.xnovo3000.eventus.mvc.repository;

import io.github.xnovo3000.eventus.bean.entity.Authority;
import io.github.xnovo3000.eventus.bean.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    void deleteAllByUser(User user);
}