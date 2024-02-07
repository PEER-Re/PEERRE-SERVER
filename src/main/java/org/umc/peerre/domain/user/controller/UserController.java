package org.umc.peerre.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.umc.peerre.domain.user.dto.response.UserTokenResponseDto;
import org.umc.peerre.domain.user.entity.User;
import org.umc.peerre.domain.user.service.UserService;
import org.umc.peerre.global.common.SuccessResponse;
import org.umc.peerre.global.config.auth.principal.PrincipalDetails;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {

    private final UserService userService;

    /**
     * 발급된 토큰 검증 테스트
     * @param principal 시큐리티를 통해 유저 정보를 받아옵니다
     * @return
     */
    @GetMapping("/test")
    public ResponseEntity<SuccessResponse<?>> test(@AuthenticationPrincipal PrincipalDetails principal) {
        User user = principal.getUser();
        return SuccessResponse.ok("닉네임 = " + user.getNickname() + ", 소셜아이디 = " + user.getSocialId());
    }
}
}