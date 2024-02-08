package org.umc.peerre.domain.feedback.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.umc.peerre.domain.feedback.controller.FeedbackController;
import org.umc.peerre.domain.project.constant.Status;
import org.umc.peerre.domain.project.entity.Project;
import org.umc.peerre.domain.project.repository.ProjectRepository;
import org.umc.peerre.domain.teamspace.entity.Teamspace;
import org.umc.peerre.domain.teamspace.entity.UserTeamspace;
import org.umc.peerre.domain.teamspace.repository.TeamspaceRepository;
import org.umc.peerre.domain.teamspace.repository.UserTeamspaceRepository;
import org.umc.peerre.global.config.auth.UserId;
import org.umc.peerre.global.config.auth.principal.PrincipalDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class MyFeedbackScheduler {

    private final FeedbackService feedbackService;
    private final UserTeamspaceRepository userTeamspaceRepository;
    private final FeedbackController feedbackController;
    private final TeamspaceRepository teamspaceRepository;
    private final ProjectRepository projectRepository;
    @Scheduled(fixedRate = 6000) // (2시간 = 7200000ms)
    public void updateMyReport() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            Long userId = principalDetails.getUser().getId();
            System.out.println(userId);
            List<UserTeamspace> userTeamspaces = userTeamspaceRepository.findByUserId(userId)
                    .orElseThrow(() -> new EntityNotFoundException("유저가 속한 팀이 없습니다.")).getTeamspace().getUserTeamspaceList();

            List<Project> projects = userTeamspaces.stream()
                    .map(UserTeamspace::getTeamspace)
                    .flatMap(teamspace -> projectRepository.findByTeamspace(teamspace).stream())
                    .toList();

            for (Project project : projects) {
                if (project.getStatus()!=null && project.getStatus().equals(Status.진행중)) {
                    Long projectId = project.getId();
                    System.out.println("userId:" + userId + "projectId:" + projectId + "에 해당하는 메서드 실행");
                    feedbackController.getMyReport(userId, projectId);
                }
            }
        } else {
            System.out.println("로그인 안되어 있음");
        }
    }
}
