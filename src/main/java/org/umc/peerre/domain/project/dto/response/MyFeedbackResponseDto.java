package org.umc.peerre.domain.project.dto.response;

import java.util.List;

public record MyFeedbackResponseDto(
        List<MyYesFeedbackResponseDto> MyYesFeedback,
        List<MyNoFeedbackResponseDto> MyNoFeedback
) {
}
