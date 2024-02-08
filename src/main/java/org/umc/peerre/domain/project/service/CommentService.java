package org.umc.peerre.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.umc.peerre.domain.project.dto.request.CreateCommentRequestDto;
import org.umc.peerre.domain.project.dto.response.comment.CommentListResponseDto;
import org.umc.peerre.domain.project.dto.response.comment.CreateCommentResponseDto;
import org.umc.peerre.domain.project.dto.response.comment.EachCommentResponseDto;
import org.umc.peerre.domain.project.entity.Comment;
import org.umc.peerre.domain.project.entity.Project;
import org.umc.peerre.domain.project.repository.CommentRepository;
import org.umc.peerre.domain.project.repository.ProjectRepository;
import org.umc.peerre.domain.user.repository.UserRepository;
import org.umc.peerre.global.error.ErrorCode;
import org.umc.peerre.global.error.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;


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
                        -> new EntityNotFoundException(ErrorCode.PROJECT_NOT_FOUND)))
                .content(content)
                .user(userRepository.findById(userId)
                        .orElseThrow(()
                        -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND))).build();

        Comment save = commentRepository.save(comment);
        return CreateCommentResponseDto.of(save);
    }

    public CommentListResponseDto getCommentList(Long projectId, Long lastCommentId, int size) {
        //프로젝트 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()
                        -> new EntityNotFoundException(ErrorCode.PROJECT_NOT_FOUND));
        //프로젝트의 회고 리스트
        Page<Comment> comments = fetchComment(project,lastCommentId, size);

        List<EachCommentResponseDto> responseDtoList = comments.stream()
                .map(comment -> EachCommentResponseDto.of(comment))
                .collect(Collectors.toList());

        return CommentListResponseDto.of(responseDtoList);
    }

    private Page<Comment> fetchComment(Project project, long lastCommentId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return commentRepository.findByProjectAndIdLessThanOrderById(project,lastCommentId, pageRequest);
    }

}
