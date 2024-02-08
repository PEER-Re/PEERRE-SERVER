package org.umc.peerre.domain.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.umc.peerre.domain.project.dto.request.CreateCommentRequestDto;
import org.umc.peerre.domain.project.dto.request.CreateProjectRequestDto;
import org.umc.peerre.domain.project.dto.response.*;
import org.umc.peerre.domain.project.dto.response.comment.CommentListResponseDto;
import org.umc.peerre.domain.project.dto.response.comment.CreateCommentResponseDto;
import org.umc.peerre.domain.project.service.CommentService;
import org.umc.peerre.domain.project.service.ProjectService;
import org.umc.peerre.global.common.SuccessResponse;
import org.umc.peerre.global.config.auth.UserId;

@RequiredArgsConstructor
@RequestMapping("/api/project")
@RestController
public class ProjectController {

    private final ProjectService projectService;
    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createProject(@RequestBody CreateProjectRequestDto createProjectRequestDto) {
        final CreateProjectResponseDto newProject = projectService.createProject(createProjectRequestDto);
        return SuccessResponse.created(newProject);
    }

    @PostMapping("/comment")
    public ResponseEntity<SuccessResponse<?>> createComments(@UserId Long userId, @RequestBody CreateCommentRequestDto createCommentRequestDto) {
        CreateCommentResponseDto newComment = commentService.createComment(userId,createCommentRequestDto);
        return SuccessResponse.created(newComment);
    }

    @PostMapping("/{projectId}")
    public ResponseEntity<SuccessResponse<?>> closeProject(@PathVariable Long projectId) {
        projectService.closeProject(projectId);
        return SuccessResponse.ok(null);
    }

    @PostMapping("/{projectId}/reopen")
    public ResponseEntity<SuccessResponse<?>> reopenProject(@PathVariable Long projectId) {
        projectService.reopenProject(projectId);
        return SuccessResponse.ok(null);
    }

    @GetMapping("/{projectId}/project-info")
    public ResponseEntity<SuccessResponse<?>> getTeamInfo(@PathVariable Long projectId) {
        final TeamInfoResponseDto teamInfoResponseDto = projectService.getTeamInfo(projectId);
        return SuccessResponse.ok(teamInfoResponseDto);
    }

    @GetMapping("/{projectId}/my-feedback")
    public ResponseEntity<SuccessResponse<?>> getMyFeedback(@UserId Long userId, @PathVariable Long projectId) {
        final MyFeedbackResponseDto myFeedbackResponseDto = projectService.getMyFeedback(userId, projectId);
        return SuccessResponse.ok(myFeedbackResponseDto);
    }

    @GetMapping("/{projectId}/comments")
    public ResponseEntity<SuccessResponse<?>> getCommentList(@PathVariable Long projectId,@RequestParam Long lastCommentId, @RequestParam int size) {
        final CommentListResponseDto commentListResponseDto = commentService.getCommentList(projectId,lastCommentId,size);
        return SuccessResponse.ok(commentListResponseDto);
    }
}
