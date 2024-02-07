package org.umc.peerre.domain.project.dto.response;

import org.umc.peerre.domain.feedback.entity.Feedback;

import java.util.List;
import java.util.stream.Collectors;

public record MyNoFeedbackResponseDto(
        String NofeedbackContent
) {
    public static MyNoFeedbackResponseDto of(Feedback feedback) {
        return new MyNoFeedbackResponseDto(feedback.getFeedbackContent());
    }
}
