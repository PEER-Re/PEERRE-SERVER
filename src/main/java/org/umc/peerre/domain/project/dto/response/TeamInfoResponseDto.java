package org.umc.peerre.domain.project.dto.response;

import java.time.LocalDate;

public record TeamInfoResponseDto(
        String teamName,

        String projectName,
        LocalDate startDay,
        LocalDate endDay,
        int size,
        double totalParticipateRate,
        int totalYesFeedbackCount,
        int totalNoFeedbackCount

) {
}
