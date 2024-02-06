package org.umc.peerre.domain.project.dto.response;

import lombok.Builder;
import org.umc.peerre.domain.project.entity.Project;
@Builder
public record CreateProjectResponseDto(
        Long projectId
) {
    public static CreateProjectResponseDto of(Project project) {
        return CreateProjectResponseDto.builder()
                .projectId(project.getId())
                .build();
    }
}
