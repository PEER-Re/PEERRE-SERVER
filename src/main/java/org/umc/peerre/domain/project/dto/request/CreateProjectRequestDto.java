package org.umc.peerre.domain.project.dto.request;

import java.time.LocalDateTime;

public record CreateProjectRequestDto(
        Long teamId,
        String title

) {

}
