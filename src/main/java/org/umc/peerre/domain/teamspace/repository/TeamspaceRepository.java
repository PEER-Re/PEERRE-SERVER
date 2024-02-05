package org.umc.peerre.domain.teamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.umc.peerre.domain.teamspace.entity.Teamspace;

public interface TeamspaceRepository extends JpaRepository<Teamspace, Long> {
}
