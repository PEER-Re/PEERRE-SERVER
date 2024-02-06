package org.umc.peerre.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.umc.peerre.domain.user.dto.response.UserTokenResponseDto;
import org.umc.peerre.global.config.auth.jwt.JwtProvider;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final JwtProvider jwtProvider;
    public UserTokenResponseDto getToken(Long userId) {
        String accessToken = issueNewAccessToken(userId);
        String refreshToken = issueNewRefreshToken(userId);
        return new UserTokenResponseDto(accessToken, refreshToken);
    }

    private String issueNewAccessToken(Long userId) {
        return jwtProvider.getIssueToken(userId, true);
    }
    private String issueNewRefreshToken(Long userId) {
        return jwtProvider.getIssueToken(userId, false);
    }

}
