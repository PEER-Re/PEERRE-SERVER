package org.umc.peerre.domain.teamspace.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.umc.peerre.domain.teamspace.dto.request.CreateTeamspaceRequestDto;
import org.umc.peerre.domain.teamspace.dto.request.TeamspaceInvitationRequestDto;
import org.umc.peerre.domain.teamspace.dto.response.*;
import org.umc.peerre.domain.teamspace.service.TeamspaceService;
import org.umc.peerre.global.common.SuccessResponse;
import org.umc.peerre.global.config.auth.UserId;

@RequiredArgsConstructor
@RequestMapping("/api/teamspace")
@RestController
public class TeamspaceController {

    private final TeamspaceService teamspaceService;

    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createTeamspace(@UserId Long userId, @RequestBody CreateTeamspaceRequestDto createTeamspaceRequestDto) {
        final CreateTeamspaceResponseDto newTeamspace = teamspaceService.createTeamspace(userId, createTeamspaceRequestDto);
        return SuccessResponse.created(newTeamspace);
    }

    @GetMapping("/teamspaces")
    public ResponseEntity<SuccessResponse<?>> getTeamspaces(@UserId Long userId) {
        final TeamspacesResponseDto teamSpacesResponseDto = teamspaceService.getTeamspaces(userId);
        return SuccessResponse.ok(teamSpacesResponseDto);
    }

    @GetMapping("/{teamspaceId}/projects")
    public ResponseEntity<SuccessResponse<?>> getProjects(@PathVariable Long teamspaceId) {
        final ProjectsResponseDto projectsResponseDto = teamspaceService.getProjects(teamspaceId);
        return SuccessResponse.ok(projectsResponseDto);
    }

    @DeleteMapping("/{teamspaceId}")
    public ResponseEntity<SuccessResponse<?>> deleteTeamspace(@UserId Long userId, @PathVariable Long teamspaceId) {
        return SuccessResponse.ok(teamspaceService.deleteTeamspace(userId, teamspaceId));
    }

    @PostMapping("/invite")
    public ResponseEntity<SuccessResponse<?>> inviteMember(@UserId Long userId, @RequestBody TeamspaceInvitationRequestDto teamspaceInvitationRequestDto) {
        return SuccessResponse.ok(teamspaceService.inviteMember(userId, teamspaceInvitationRequestDto));
    }

}
