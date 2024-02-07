package org.umc.peerre.domain.project.dto.response.comment;

import lombok.Builder;

import java.util.List;

@Builder
public record CommentListResponseDto(
        List<EachCommentResponseDto> commentList
) { public static CommentListResponseDto of(List<EachCommentResponseDto> commentResponseDtos) {

    return CommentListResponseDto.builder()
            .commentList(commentResponseDtos).build();

}
}
