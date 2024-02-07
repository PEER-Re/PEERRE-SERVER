package org.umc.peerre.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.umc.peerre.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialId(String email);

}
