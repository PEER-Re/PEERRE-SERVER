package org.umc.peerre.domain.project.dto.response.comment;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.umc.peerre.domain.project.entity.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@Slf4j
public record CommentListResponseDto(
        boolean isEmpty,
        int size,
        List<EachCommentResponseDto> commentList
) { public static CommentListResponseDto of(Slice<EachCommentResponseDto> comments) {

    List<EachCommentResponseDto> responseDtos = new ArrayList<>(comments.getContent());
    Collections.reverse(responseDtos);

    return CommentListResponseDto.builder()
            .isEmpty(comments.isEmpty())
            .size(comments.getNumberOfElements())
            .commentList(responseDtos).build();
}}
