package org.umc.peerre.domain.teamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.umc.peerre.domain.teamspace.entity.UserTeamspace;

import java.util.Optional;

@Repository
public interface UserTeamspaceRepository extends JpaRepository<UserTeamspace, Long> {

    Optional<UserTeamspace> findByUserId(Long userId);
}
