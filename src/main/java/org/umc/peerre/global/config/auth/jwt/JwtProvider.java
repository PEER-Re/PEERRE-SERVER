package org.umc.peerre.global.config.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.umc.peerre.global.error.ErrorCode;
import org.umc.peerre.global.error.exception.UnauthorizedException;

import java.util.Date;

@Getter
@Component
@Slf4j
public class JwtProvider {

    private static final String PREFIX = "Bearer ";
    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.access-token-expire-time}")
    private long ACCESS_TOKEN_EXPIRE_TIME;

    /**
     * @param socialId 토큰에 담아줄 정보
     * @return 액세스토큰
     */
    public String createAccessToken(String socialId) {
        return PREFIX + JWT.create()
                .withSubject("AccessToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .withClaim("socialId", socialId)
                .sign(Algorithm.HMAC512(SECRET));
    }

    public void validateAccessToken(String accessToken) {
        try {
            JWT.require(Algorithm.HMAC512(SECRET))
                    .build()
                    .verify(accessToken);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (Exception e) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN_VALUE);
        }
    }

    public String extractSocialId(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET)).build()
                .verify(token)
                .getClaim("socialId").asString();
    }
}