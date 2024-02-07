package org.umc.peerre.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.umc.peerre.domain.project.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
