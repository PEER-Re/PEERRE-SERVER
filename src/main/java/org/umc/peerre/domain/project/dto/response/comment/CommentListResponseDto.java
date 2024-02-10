package org.umc.peerre.domain.project.dto.response.comment;

import lombok.Builder;
import org.springframework.data.domain.Slice;
import org.umc.peerre.domain.project.entity.Comment;

import java.util.List;

@Builder
public record CommentListResponseDto(
        boolean isEmpty,
        int size,
        List<EachCommentResponseDto> commentList
) { public static CommentListResponseDto of(Slice<EachCommentResponseDto> comments) {

    return CommentListResponseDto.builder()
            .isEmpty(comments.isEmpty())
            .size(comments.getNumberOfElements())
            .commentList(comments.getContent()).build();
}}
