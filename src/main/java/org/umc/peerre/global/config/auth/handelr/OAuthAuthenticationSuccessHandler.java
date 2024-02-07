package org.umc.peerre.global.config.auth.handelr;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.umc.peerre.global.config.auth.jwt.JwtProvider;
import org.umc.peerre.global.config.auth.principal.PrincipalDetails;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private final JwtProvider jwtProvider;
    private String Authorization ="Authorization";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        String socialId = principal.getSocialId();
        String accessToken = jwtProvider.createAccessToken(socialId);
        //토큰 담아주는 방식 논의 필요
        response.addHeader(Authorization, accessToken);
        log.info(accessToken); //삭제 예정
        //프론트엔드 주소로 바꾸어 리다이렉트한다.
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:8080/test");
    }

    //톰캣 서버 정책으로 인해 공백과 특수문자를 쿠키에 담을 수 없음
    //URLEncoder사용 시 의논 필요 - 공백-> +로 치환 , 다른 특수문자를 치환하지는 않는지 ?
    private Cookie setCookie(String key, String accesstoken) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(accesstoken, "utf-8");
        Cookie cookie = new Cookie(key, encode);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
