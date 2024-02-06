package org.umc.peerre.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.umc.peerre.domain.project.constant.Status;
import org.umc.peerre.domain.project.entity.Project;
import org.umc.peerre.domain.teamspace.entity.Teamspace;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Object> findByTeamspaceAndStatus(Teamspace teamspace, Status status);
}
