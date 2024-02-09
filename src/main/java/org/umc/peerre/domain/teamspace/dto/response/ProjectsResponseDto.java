package org.umc.peerre.domain.teamspace.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProjectsResponseDto(
        List<ProjectResponseDto> projectResponseDtoList
) {
    public static ProjectsResponseDto of(List<ProjectResponseDto> projectResponseDtoList) {

        return ProjectsResponseDto.builder()
                .projectResponseDtoList(projectResponseDtoList)
                .build();
    }
}
