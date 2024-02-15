package org.umc.peerre.global.config.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.umc.peerre.domain.user.entity.User;
import org.umc.peerre.domain.user.repository.UserRepository;
import org.umc.peerre.global.config.auth.principal.PrincipalDetails;
import org.umc.peerre.global.error.ErrorCode;
import org.umc.peerre.global.error.exception.UnauthorizedException;
import java.io.IOException;
import static org.umc.peerre.global.error.ErrorCode.MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private String accessHeader = "Authorization";
    private static final String PREFIX = "Bearer ";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessTokenFromHttpServletRequest(request);

        jwtProvider.validateAccessToken(accessToken);
        String socialId = jwtProvider.extractSocialId(accessToken);
        setAuthentication(socialId);
        filterChain.doFilter(request, response);
    }

    private String getAccessTokenFromHttpServletRequest(HttpServletRequest request) {

        String accessToken = request.getHeader(accessHeader);

        //AUTHORIZATION 헤더가 없을 경우
        if (accessToken == null) {
            throw new UnauthorizedException(ErrorCode.NONE_AUTHORIZATION_HEADER);
        }
        //Bear로 시작하지 않는 경우
        if (!accessToken.startsWith(PREFIX)) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        return accessToken.replace(PREFIX, "");
    }

    private void setAuthentication(String socialId) {
        User user = userRepository.findBySocialId(socialId)
                .orElseThrow(()-> new UnauthorizedException(MEMBER_NOT_FOUND));

        // user를 세션에 저장하기 위해 authentication 객체를 생성한다.
        PrincipalDetails principalDetails = new PrincipalDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        // 시큐리티의 세션에 authentication 객체를 저장한다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
