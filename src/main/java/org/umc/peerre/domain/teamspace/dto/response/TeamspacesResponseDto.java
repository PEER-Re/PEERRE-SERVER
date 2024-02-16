package org.umc.peerre.domain.teamspace.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record TeamspacesResponseDto(
        List<TeamspaceResponseDto> teamspaceResponseDtoList
) {
    public static TeamspacesResponseDto of(List<TeamspaceResponseDto> teamspaceResponseDtoList) {

        return TeamspacesResponseDto.builder()
                .teamspaceResponseDtoList(teamspaceResponseDtoList)
                .build();
    }
}
