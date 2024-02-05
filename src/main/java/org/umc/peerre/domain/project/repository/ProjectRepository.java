package org.umc.peerre.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.umc.peerre.domain.project.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
