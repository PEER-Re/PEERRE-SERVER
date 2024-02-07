package org.umc.peerre.domain.feedback.dto.request;

public record CreateCommentRequestDto(
        Long projectId,
        String content
) {
}
