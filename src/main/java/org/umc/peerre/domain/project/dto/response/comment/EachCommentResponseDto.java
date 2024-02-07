package org.umc.peerre.domain.project.dto.response.comment;

import lombok.Builder;
import org.umc.peerre.domain.project.entity.Comment;
import org.umc.peerre.domain.user.entity.User;

@Builder
public record EachCommentResponseDto(
        String nickname,
        String profileImgUrl,
        String content
) { public static EachCommentResponseDto of(Comment comment) {

    User user = comment.getUser();

    return EachCommentResponseDto.builder()
            .nickname(user.getNickname())
            .profileImgUrl(user.getProfileImgUrl())
            .content(comment.getContent()).build();
    }
}
