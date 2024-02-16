package org.umc.peerre.domain.project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.umc.peerre.domain.project.dto.response.comment.EachCommentResponseDto;
import org.umc.peerre.domain.project.entity.Comment;
import org.umc.peerre.domain.project.entity.Project;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {


}
