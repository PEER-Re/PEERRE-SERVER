package org.umc.peerre.domain.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.umc.peerre.domain.project.dto.request.CreateCommentRequestDto;
import org.umc.peerre.domain.project.dto.response.CreateCommentResponseDto;
import org.umc.peerre.domain.project.service.CommentService;
import org.umc.peerre.global.common.SuccessResponse;
import org.umc.peerre.global.config.auth.UserId;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<SuccessResponse<?>> createComments(@UserId Long userId, @RequestBody CreateCommentRequestDto createCommentRequestDto) {
        CreateCommentResponseDto newComment = commentService.createComment(userId,createCommentRequestDto);
        return SuccessResponse.created(newComment);
    }

}
