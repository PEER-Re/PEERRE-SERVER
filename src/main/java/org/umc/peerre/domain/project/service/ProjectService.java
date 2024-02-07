package org.umc.peerre.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.umc.peerre.domain.feedback.entity.FeedbackAggregation;
import org.umc.peerre.domain.feedback.entity.FeedbackRegistration;
import org.umc.peerre.domain.project.dto.request.CreateProjectRequestDto;
import org.umc.peerre.domain.project.constant.Status;
import org.umc.peerre.domain.project.dto.response.CreateProjectResponseDto;
import org.umc.peerre.domain.project.dto.response.TeamInfoResponseDto;
import org.umc.peerre.domain.project.entity.Project;
import org.umc.peerre.domain.project.repository.ProjectRepository;
import org.umc.peerre.domain.teamspace.entity.Teamspace;
import org.umc.peerre.domain.teamspace.repository.TeamspaceRepository;
import org.umc.peerre.global.error.ErrorCode;
import org.umc.peerre.global.error.exception.EntityNotFoundException;
import org.umc.peerre.global.error.exception.InvalidValueException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.umc.peerre.global.error.ErrorCode.*;

@RequiredArgsConstructor
@Service
@Transactional
public class ProjectService {

    private final TeamspaceRepository teamspaceRepository;
    private final ProjectRepository projectRepository;
    public CreateProjectResponseDto createProject(CreateProjectRequestDto createProjectRequestDto) {
        Long teamId = createProjectRequestDto.teamId();
        String title = createProjectRequestDto.title();

        Teamspace teamspace = teamspaceRepository.findById(teamId).orElseThrow(()
                -> new EntityNotFoundException(TEAM_NOT_FOUND));

        if (projectRepository.findByTeamspaceAndStatus(teamspace, Status.진행중).isPresent()) {
            throw new InvalidValueException(PROJECT_ALREADY_IN_PROGRESS);
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

        return CreateProjectResponseDto.of(project);
    }

    public void closeProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PROJECT_NOT_FOUND));
        project.setStatus(Status.종료);
        project.setEndDay(LocalDate.now());
    }

    public void reopenProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        Long teamId = project.getTeamspace().getId();
        if (projectRepository.findByTeamspaceIdAndStatus(teamId,Status.진행중).isPresent()) {
            throw new InvalidValueException(PROJECT_ALREADY_IN_PROGRESS);
        }

        Optional<Project> latestProject = projectRepository.findFirstByTeamspaceIdAndStatusOrderByEndDayDesc(teamId, Status.종료);
        System.out.println(latestProject.get().getId());
        if (latestProject.isEmpty() || !latestProject.get().getId().equals(projectId)) {
            throw new InvalidValueException(NOT_LASTEST_PROJECT);
        }

        project.setStatus(Status.진행중);
        project.setEndDay(null);
    }

    public TeamInfoResponseDto getTeamInfo(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        List<FeedbackAggregation> feedbackAggregationList = project.getFeedbackAggregationList();
        List<FeedbackRegistration> feedbackRegistrationList = project.getFeedbackRegistrationList();

        double totalParticipateRate = calculateTotalParticipateRate(feedbackAggregationList);

        int totalYesFeedbackCount = calculateTotalFeedbackCount(feedbackRegistrationList, true);
        int totalNoFeedbackCount = calculateTotalFeedbackCount(feedbackRegistrationList, false);

        return new TeamInfoResponseDto(
                project.getTeamspace().getName(),
                project.getStartDay(),
                project.getEndDay(),
                project.getSize(),
                totalParticipateRate * 100.0,
                totalYesFeedbackCount,
                totalNoFeedbackCount
        );
    }

    private double calculateTotalParticipateRate(List<FeedbackAggregation> feedbackAggregationList) {
        long totalFeedbackCount = feedbackAggregationList.size();
        long evaluatedFeedbackCount = feedbackAggregationList.stream()
                .filter(feedbackAggregation -> feedbackAggregation.getEvaluation_status())
                .count();

        return totalFeedbackCount > 0 ? (double) evaluatedFeedbackCount / totalFeedbackCount : 0.0;
    }

    private int calculateTotalFeedbackCount(List<FeedbackRegistration> feedbackRegistrationList, boolean isYes) {
        return (int) feedbackRegistrationList.stream()
                .flatMap(feedbackRegistration -> feedbackRegistration.getFeedbackList().stream())
                .filter(feedback -> isYes == feedback.getFeedback_type())
                .count();
    }



}
