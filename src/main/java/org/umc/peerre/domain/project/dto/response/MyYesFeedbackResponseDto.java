package org.umc.peerre.domain.project.dto.response;

import org.umc.peerre.domain.feedback.entity.Feedback;

import java.util.List;
import java.util.stream.Collectors;

public record MyYesFeedbackResponseDto(
        String yesFeedbackContent
) {
    public static MyYesFeedbackResponseDto of(Feedback feedback) {
        return new MyYesFeedbackResponseDto(feedback.getFeedbackContent());
    }

}
