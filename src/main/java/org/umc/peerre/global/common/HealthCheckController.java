package org.umc.peerre.global.common;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @Hidden
    @RequestMapping("/")
    public String PeerreServer() {
        return "피어리 서버입니다.";
    }
}
