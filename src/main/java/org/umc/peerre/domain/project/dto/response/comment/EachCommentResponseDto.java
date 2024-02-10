package org.umc.peerre.domain.project.dto.response.comment;

import lombok.Builder;
import org.umc.peerre.domain.project.entity.Comment;
import org.umc.peerre.domain.user.entity.User;

@Builder
public record EachCommentResponseDto(
        long id,
        String nickname,
        String profileImgUrl,
        String content
)
{

}
