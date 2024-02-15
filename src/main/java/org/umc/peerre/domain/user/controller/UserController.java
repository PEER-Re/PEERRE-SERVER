package org.umc.peerre.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.umc.peerre.domain.user.entity.User;
import org.umc.peerre.domain.user.service.UserService;
import org.umc.peerre.global.common.SuccessResponse;
import org.umc.peerre.global.config.auth.UserId;


@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {

    private final UserService userService;

    /**
     * 발급된 토큰 검증 테스트 api
     * @param userId 토큰으로부터 추출해온 User 정보로 꺼내온 User 객체의 아이디
     * @return
     */
    @GetMapping("/test")
    public ResponseEntity<SuccessResponse<?>> test(@UserId Long userId) {
        return SuccessResponse.ok("유저 아이디 = "+userId);
    }

    @GetMapping("/healthcheck")
    public String PeerreServer() {
        return "피어리 서버입니다.";
    }
}

