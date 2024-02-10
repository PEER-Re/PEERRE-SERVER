package org.umc.peerre.global.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.umc.peerre.global.error.ErrorCode;
import org.umc.peerre.global.error.dto.ErrorResponse;
import org.umc.peerre.global.error.exception.InvalidValueException;
import org.umc.peerre.global.error.exception.TemporaryException;
import org.umc.peerre.global.error.exception.UnauthorizedException;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //JwtAuthenticationFilter 실행
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException e) {
            handleUnauthorizedException(response, e);
        } catch (TemporaryException e) {
            handleTempException(request, response, e);
        } catch (Exception e) {
            handleException(response);
        }
    }

    /**
     * 프론트 서버와 연결 시 삭제, 카카오 로그인 성공 시 일시적으로 백엔드 서버로 리다이렉트 하므로 발생하는 오류
     * ! 삭제 예정 !
     */
    private void handleTempException(HttpServletRequest request,HttpServletResponse response,TemporaryException e) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", e.getMessage());
        body.put("message", "oauth 테스트 로그인입니다. 카카오 로그인을 진행하였다면 콘솔창에 출력된 log에서 발급된 토큰을 확인할 수 있습니다.");
        body.put("path", request.getServletPath());
        objectMapper.writeValue(response.getOutputStream(), body);
        response.setStatus(HttpServletResponse.SC_OK);
    }
    private void handleUnauthorizedException(HttpServletResponse response, Exception e) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        if (e instanceof UnauthorizedException ue) {
            response.setStatus(ue.getErrorCode().getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(ue.getErrorCode())));
        } else if (e instanceof InvalidValueException ie) {
            response.setStatus(ie.getErrorCode().getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(ie.getErrorCode())));
        }
    }

    private void handleException(HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR)));
    }
}