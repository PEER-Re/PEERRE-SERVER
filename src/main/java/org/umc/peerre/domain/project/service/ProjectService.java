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

import java.time.LocalDate;
import java.time.LocalDateTime;

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
                -> new IllegalArgumentException("존재하지 않는 팀입니다."));
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
