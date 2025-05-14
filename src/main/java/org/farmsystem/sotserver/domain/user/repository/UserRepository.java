package org.farmsystem.sotserver.domain.user.repository;

import org.farmsystem.sotserver.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

}
