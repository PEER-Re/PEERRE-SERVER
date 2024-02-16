package org.umc.peerre.domain.project.dto.response.comment;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.umc.peerre.domain.project.entity.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
public record CommentListResponseDto(
        List<EachCommentResponseDto> commentList
) { public static CommentListResponseDto of(List<EachCommentResponseDto> commentResponseDtos) {

    return CommentListResponseDto.builder()
            .commentList(commentResponseDtos).build();

  }
}
