package org.umc.peerre.domain.feedback.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertFalse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class FeedbackRequest {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Feedback {
        @Schema(description = "연락이 잘 돼요: true, 안 돼요: false", nullable = false, example = "true")
        private Boolean communication;
        @Schema(description = "시간약속을 잘 지켜요: true, 안지켜요 : false", nullable = false, example = "true")
        private Boolean punctual;
        @Schema(description = "능력이 뛰어나요: true, 뒤떨어져요: false", nullable = false, example = "true")
        private Boolean competent;
        @Schema(description = "말을 조리있게 잘해요: true, 못해요: false", nullable = false, example = "true")
        private Boolean articulate;
        @Schema(description = "빈틈이 없어요: true, 있어요: false", nullable = false, example = "true")
        private Boolean thorough;
        @Schema(description = "재미있어요: true, 없어요: false", nullable = false, example = "true")
        private Boolean engaging;
    }
}
