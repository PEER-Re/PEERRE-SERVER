package org.umc.peerre.domain.teamspace.dto.response;

import lombok.Builder;
import org.umc.peerre.domain.teamspace.constant.Role;
import org.umc.peerre.domain.teamspace.entity.UserTeamspace;

@Builder
public record TeamspaceResponseDto(
        String name,

        String profile,

        int size,

        Role role

) {

    public static TeamspaceResponseDto of(UserTeamspace userTeamspace) {

        String name = userTeamspace.getTeamspace().getName();

        String profile = userTeamspace.getTeamspace().getProfile();

        int size = userTeamspace.getTeamspace().getSize();

        return TeamspaceResponseDto.builder()
                .name(name)
                .profile(profile)
                .size(size)
                .role(userTeamspace.getRole())
                .build();
    }
}
