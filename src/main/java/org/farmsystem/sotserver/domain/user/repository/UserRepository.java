package org.farmsystem.sotserver.domain.user.repository;

import org.farmsystem.sotserver.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findBySocialId(String socialId);
    Optional<User> findByEmail(String email);
}
