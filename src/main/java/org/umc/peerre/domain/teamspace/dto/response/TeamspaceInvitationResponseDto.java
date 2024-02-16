package org.umc.peerre.domain.teamspace.dto.response;

import lombok.Builder;
import org.umc.peerre.domain.teamspace.constant.Role;
import org.umc.peerre.domain.teamspace.entity.Teamspace;
import org.umc.peerre.domain.teamspace.entity.UserTeamspace;

@Builder
public record TeamspaceInvitationResponseDto(
        Long userId,
        String name,
        String profile,
        Role role,
        int size
) {
    public static TeamspaceInvitationResponseDto of(Long userId, UserTeamspace userTeamspace, Teamspace teamspace) {
        return TeamspaceInvitationResponseDto.builder()
                .userId(userId)
                .name(teamspace.getName())
                .profile(teamspace.getProfile())
                .role(userTeamspace.getRole())
                .size(teamspace.getSize())
                .build();
    }
}
