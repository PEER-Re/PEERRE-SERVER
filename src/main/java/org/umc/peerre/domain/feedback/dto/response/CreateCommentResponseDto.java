package org.umc.peerre.domain.feedback.dto.response;

import lombok.Builder;
import org.umc.peerre.domain.feedback.entity.Comment;

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
