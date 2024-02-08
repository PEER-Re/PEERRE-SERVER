package org.umc.peerre.domain.teamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.umc.peerre.domain.teamspace.entity.Teamspace;
import org.umc.peerre.domain.teamspace.entity.UserTeamspace;
import org.umc.peerre.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTeamspaceRepository extends JpaRepository<UserTeamspace, Long> {

    Optional<UserTeamspace> findByUserId(Long userId);
    Optional<List<UserTeamspace>> findByTeamspace(Teamspace teamspace);

    Optional<List<UserTeamspace>> findByUser(User user);
}
