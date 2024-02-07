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
import org.umc.peerre.global.error.exception.TemporaryException;
import org.umc.peerre.global.error.exception.UnauthorizedException;

import java.io.IOException;

import static org.umc.peerre.global.error.ErrorCode.BAD_REQUEST;

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

        // !!! 프론트 연결 시 삭제 !!! -------------- //
        if (request.getRequestURI().equals("/test")) {
            throw new TemporaryException(ErrorCode.TEMPORARY_ERROR);
        }
        // -------------------------------- 삭제 끝 //

        String accessToken = request.getHeader(accessHeader);

        if (StringUtils.hasText(accessToken) && accessToken.startsWith(PREFIX)) {
            return accessToken.replace(PREFIX, "");
        }
        throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
    }

    private void setAuthentication(String socialId) {
        User user = userRepository.findBySocialId(socialId)
                .orElseThrow(()-> new UnauthorizedException(BAD_REQUEST));
        // user를 세션에 저장하기 위해 authentication 객체를 생성한다.
        PrincipalDetails principalDetails = new PrincipalDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        // 시큐리티의 세션에 authentication 객체를 저장한다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
