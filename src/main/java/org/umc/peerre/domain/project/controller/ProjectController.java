package org.umc.peerre.domain.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.umc.peerre.domain.project.dto.request.CreateProjectRequestDto;
import org.umc.peerre.domain.project.service.ProjectService;
import org.umc.peerre.global.common.SuccessResponse;
import org.umc.peerre.global.config.auth.UserId;

@RequiredArgsConstructor
@RequestMapping("/api/project")
@RestController
public class ProjectController {

    private final ProjectService projectService;
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createProject(@RequestBody CreateProjectRequestDto createProjectRequestDto) {
        final CreateProjectRequestDto newProject = projectService.createProject(createProjectRequestDto);
        return SuccessResponse.created(newProject);
    }

    @PostMapping("/{projectId}")
    public ResponseEntity<SuccessResponse<?>> closeProject(@PathVariable Long projectId) {
        projectService.closeProject(projectId);
        return SuccessResponse.ok(null);
    }
}
