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

    @Query("SELECT new org.umc.peerre.domain.project.dto.response.comment.EachCommentResponseDto(c.id, c.user.nickname, c.user.profileImgUrl, c.content)" +
            " FROM Comment c WHERE c.project = :project AND c.id < :id ORDER BY c.id DESC")
    Slice<EachCommentResponseDto> fetchComments(@Param("project") Project project, @Param("id") Long id, Pageable pageable);


    @Query("SELECT c.id FROM Comment c WHERE c.project = :project ORDER BY c.id DESC")
    List<Long> findCommentIdsByDesc(@Param("project") Project project);

}
