package org.umc.peerre.domain.teamspace.dto.response;

import lombok.Builder;
import org.umc.peerre.domain.project.constant.Status;
import org.umc.peerre.domain.project.entity.Project;

import java.time.LocalDate;

@Builder
public record ProjectResponseDto(
        String title,

        Status status,

        LocalDate startDay,

        LocalDate endDay
) {
    public static ProjectResponseDto of(Project project) {

        String title = project.getTitle();

        Status status = project.getStatus();

        LocalDate startDay = project.getStartDay();

        LocalDate endDay = project.getEndDay();

        return ProjectResponseDto.builder()
                .title(title)
                .status(status)
                .startDay(startDay)
                .endDay(endDay)
                .build();
    }
}
