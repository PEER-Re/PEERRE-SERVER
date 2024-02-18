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
        @Schema(description = "프로젝트 관련 소통이 원활함: true, No: false", nullable = true, example = "true")
        private Boolean communication;
        @Schema(description = "정해진 기한 내 업무를 달성함: true, No : false", nullable = true, example = "true")
        private Boolean punctual;
        @Schema(description = "업무 달성 결과의 질적 수준이 높음: true, No: false", nullable = true, example = "true")
        private Boolean competent;
        @Schema(description = "동료의 의견을 적극 수용함: true, No: false", nullable = true, example = "true")
        private Boolean articulate;
        @Schema(description = "업무를 책임감 있게 수행함: true, No: false", nullable = true, example = "true")
        private Boolean thorough;
        @Schema(description = "프로젝트에 적극적인 자세로 참여함: true, No: false", nullable = true, example = "true")
        private Boolean engaging;
    }
}
