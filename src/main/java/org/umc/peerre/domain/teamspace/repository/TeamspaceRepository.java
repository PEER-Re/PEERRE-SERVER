package org.umc.peerre.domain.teamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.umc.peerre.domain.teamspace.entity.Teamspace;

import java.util.Optional;

@Repository
public interface TeamspaceRepository extends JpaRepository<Teamspace, Long> {
    Optional<Teamspace> findByInvitationCode(String invitationCode);
}
