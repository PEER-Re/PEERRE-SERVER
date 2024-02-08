package org.umc.peerre.domain.project.dto.response.comment;

import lombok.Builder;
import org.umc.peerre.domain.project.entity.Comment;

@Builder
public record CreateCommentResponseDto(
        Long commentId
) {
    public static CreateCommentResponseDto of(Comment comment) {
        return CreateCommentResponseDto.builder()
                .commentId(comment.getId())
                .build();
    }
}
