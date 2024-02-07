package org.umc.peerre.domain.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.umc.peerre.domain.feedback.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
