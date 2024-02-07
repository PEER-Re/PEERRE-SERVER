package org.umc.peerre.domain.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.umc.peerre.domain.feedback.dto.request.CreateCommentRequestDto;
import org.umc.peerre.domain.feedback.dto.response.CreateCommentResponseDto;
import org.umc.peerre.domain.feedback.entity.Comment;
import org.umc.peerre.domain.feedback.repository.CommentRepository;
import org.umc.peerre.domain.project.repository.ProjectRepository;
import org.umc.peerre.domain.user.repository.UserRepository;
import org.umc.peerre.global.error.ErrorCode;
import org.umc.peerre.global.error.exception.EntityNotFoundException;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public CreateCommentResponseDto createComment(Long userId, CreateCommentRequestDto createCommentRequestDto) {
        Long projectId = createCommentRequestDto.projectId();
        String content = createCommentRequestDto.content();

        Comment comment = Comment.builder()
                .project(projectRepository.findById(projectId)
                        .orElseThrow(()
                        -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND)))
                .content(content)
                .user(userRepository.findById(userId)
                        .orElseThrow(()
                        -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND))).build();

        Comment save = commentRepository.save(comment);
        return CreateCommentResponseDto.of(save);
    }
}
