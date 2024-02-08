package org.umc.peerre.domain.teamspace.dto.response;

import lombok.Builder;
import org.umc.peerre.domain.teamspace.entity.Teamspace;

@Builder
public record CreateTeamspaceResponseDto (
        Long teamspaceId
) {
    public static CreateTeamspaceResponseDto of(Teamspace teamspace) {
        return CreateTeamspaceResponseDto.builder()
                .teamspaceId(teamspace.getId())
                .build();
    }
}
