package org.umc.peerre.domain.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.umc.peerre.domain.project.entity.Comment;
import org.umc.peerre.domain.project.entity.Project;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Page<Comment> findByProjectAndIdLessThanOrderById(Project project,Long id, Pageable pageable);
}
