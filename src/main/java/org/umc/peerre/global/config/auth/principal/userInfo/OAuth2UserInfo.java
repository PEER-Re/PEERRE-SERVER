package org.umc.peerre.global.config.auth.principal.userInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuth2UserInfo {
    private final String nickname;
    private final String profileImgUrl;
    private final String email;
}
