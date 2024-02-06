package org.umc.peerre.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.umc.peerre.domain.project.dto.request.CreateProjectRequestDto;
import org.umc.peerre.domain.project.constant.Status;
import org.umc.peerre.domain.project.entity.Project;
import org.umc.peerre.domain.project.repository.ProjectRepository;
import org.umc.peerre.domain.teamspace.entity.Teamspace;
import org.umc.peerre.domain.teamspace.repository.TeamspaceRepository;
import org.umc.peerre.global.error.ErrorCode;
import org.umc.peerre.global.error.exception.EntityNotFoundException;
import org.umc.peerre.global.error.exception.InvalidValueException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.umc.peerre.global.error.ErrorCode.TEAM_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
public class ProjectService {

    private final TeamspaceRepository teamspaceRepository;
    private final ProjectRepository projectRepository;
    public CreateProjectRequestDto createProject(CreateProjectRequestDto createProjectRequestDto) {
        Long teamId = createProjectRequestDto.teamId();
        String title = createProjectRequestDto.title();

        Teamspace teamspace = teamspaceRepository.findById(teamId).orElseThrow(()
                -> new EntityNotFoundException(TEAM_NOT_FOUND));

        if (projectRepository.findByTeamspaceAndStatus(teamspace, Status.진행중).isPresent()) {
            throw new InvalidValueException(ErrorCode.PROJECT_ALREADY_IN_PROGRESS);
        }


        int size = teamspace.getSize();

        Project project = Project.builder()
                .teamspace(teamspace)
                .title(title)
                .status(Status.진행중)
                .startDay(LocalDate.now())
                .endDay(null)
                .size(size)
                .build();

        projectRepository.save(project);

        return null;
    }


}
