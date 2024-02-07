package org.umc.peerre.domain.project.dto.request;

public record CreateCommentRequestDto(
        Long projectId,
        String content
) {
}
