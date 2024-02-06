package org.umc.peerre.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.umc.peerre.domain.user.dto.response.UserTokenResponseDto;
import org.umc.peerre.global.common.SuccessResponse;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<SuccessResponse<?>> test() {
        String response = "Test";
        return SuccessResponse.ok(response);
    }

    // 임시 발급 API 입니다. 추후 로그인 기능이 완성되면 삭제할 예정입니다.
    @PostMapping("/token/{userId}")
    public ResponseEntity<SuccessResponse<?>> getToken(@PathVariable Long userId) {
        final UserTokenResponseDto userTokenResponseDto = userService.getToken(userId);
        return SuccessResponse.created(userTokenResponseDto);
    }
}