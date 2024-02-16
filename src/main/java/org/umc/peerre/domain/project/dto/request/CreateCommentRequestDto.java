package org.umc.peerre.domain.project.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateCommentRequestDto(
        Long projectId,
        @NotNull String content
) {
}
