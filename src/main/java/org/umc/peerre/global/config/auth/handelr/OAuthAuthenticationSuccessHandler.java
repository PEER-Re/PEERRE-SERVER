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
        response.addHeader(Authorization, accessToken);
        getRedirectStrategy().sendRedirect(request, response, "https://peerre-front.vercel.app/team-space");
    }
}
