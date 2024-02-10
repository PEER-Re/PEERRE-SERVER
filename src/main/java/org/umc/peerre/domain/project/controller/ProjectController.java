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

    /**
     * @TODO size=0인 요청 핸들링
     * @param projectId
     * @param lastCommentId
     * @param size
     * @return
     */
    @GetMapping("/{projectId}/comments")
    public ResponseEntity<SuccessResponse<?>> getCommentList(@PathVariable Long projectId
            , @RequestParam(required = false) Long lastCommentId, @RequestParam(required = false, defaultValue = "10") int size) {

        //lastCommentId가 없다는 것은 유저가 맨 처음 보게 될 상태의 데이터를 반환해야한다는 것을 의마하는데,
        //이때 맨 처음 상태 상황은 두 가지이다.
        // 1) 아무도 회고를 작성하지 않은 상태
        // 2) 작성된 회고가 있는 상태

        final CommentListResponseDto commentListResponseDto = commentService.getCommentList(projectId,lastCommentId,size);
        return SuccessResponse.ok(commentListResponseDto);
    }
}
