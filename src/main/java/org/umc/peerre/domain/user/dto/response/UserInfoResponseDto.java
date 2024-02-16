package org.umc.peerre.domain.user.dto.response;

import lombok.Builder;
import org.umc.peerre.domain.user.entity.User;

@Builder
public record UserInfoResponseDto(
        String nickname,
        String profileImgUrl
) {
    public static UserInfoResponseDto of(User user) {
        return UserInfoResponseDto.builder()
                .nickname(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl()).build();
    }
}
