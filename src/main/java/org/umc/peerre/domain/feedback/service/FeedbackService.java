package org.umc.peerre.domain.feedback.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.umc.peerre.domain.feedback.dto.request.FeedbackRequest;
import org.umc.peerre.domain.feedback.entity.Feedback;
import org.umc.peerre.domain.feedback.entity.FeedbackAggregation;
import org.umc.peerre.domain.feedback.entity.FeedbackRegistration;
import org.umc.peerre.domain.feedback.repository.FeedbackAggregationRepository;
import org.umc.peerre.domain.feedback.repository.FeedbackRegistrationRepository;
import org.umc.peerre.domain.feedback.repository.FeedbackRepository;
import org.umc.peerre.domain.project.entity.Project;
import org.umc.peerre.domain.project.repository.ProjectRepository;
import org.umc.peerre.domain.user.entity.User;
import org.umc.peerre.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Transactional
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final FeedbackRegistrationRepository feedbackRegistrationRepository;
    private final FeedbackAggregationRepository feedbackAggregationRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public String enrollFeedback(Long userId, Long teamMemberId, Long projectId, FeedbackRequest.Feedback request) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));
        User teamMember = userRepository.findById(teamMemberId).orElseThrow(() -> new EntityNotFoundException("해당하는 팀원이 없습니다."));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("해당하는 프로젝트가 없습니다."));

        if (!feedbackRegistrationRepository.existsByRecipientIdAndUserAndProject(teamMemberId, user, project)) {

            FeedbackRegistration feedbackRegistration = FeedbackRegistration.builder()
                    .recipientId(teamMemberId)
                    .user(user)
                    .project(project)
                    .build();

            FeedbackAggregation feedbackAggregation = FeedbackAggregation.builder()
                    .user(user)
                    .project(project)
                    .build();

            if (request != null) {
                List<Feedback> feedbackList = new ArrayList<>();

                if (request.getCommunication() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedbackContent(request.getCommunication() ? "연락이 잘 돼요" : "연락이 안 돼요")
                            .feedbackType(request.getCommunication())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                if (request.getPunctual() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedbackContent(request.getPunctual() ? "시간약속을 잘 지켜요" : "시간약속을 잘 안지켜요")
                            .feedbackType(request.getPunctual())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                if (request.getCompetent() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedbackContent(request.getCompetent() ? "능력이 뛰어나요" : "능력이 뒤떨어져요")
                            .feedbackType(request.getCompetent())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                if (request.getArticulate() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedbackContent(request.getArticulate() ? "말을 조리있게 잘해요" : "말을 조리있게 못해요")
                            .feedbackType(request.getArticulate())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                if (request.getThorough() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedbackContent(request.getThorough() ? "빈틈이 없어요" : "빈틈이 있어요")
                            .feedbackType(request.getThorough())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                if (request.getEngaging() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedbackContent(request.getEngaging() ? "재미있어요" : "재미없어요")
                            .feedbackType(request.getEngaging())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                feedbackRepository.saveAll(feedbackList);
            }

            feedbackRegistrationRepository.save(feedbackRegistration);

            if (feedbackAggregation.getEvaluationStatus() == null) {
                feedbackAggregation.setEvaluationStatus(false);
            }

            if (!feedbackAggregation.getEvaluationStatus()) {
                feedbackAggregation.setEvaluationStatus(true);
            }
            feedbackAggregationRepository.save(feedbackAggregation);
        } else {
            FeedbackRegistration feedbackRegistration = feedbackRegistrationRepository.findByRecipientIdAndUserAndProject(teamMemberId, user, project);

            List<Feedback> feedbackList = feedbackRegistration.getFeedbackList();

            feedbackList.forEach(feedback -> {
                if ((feedback.getFeedbackContent().equals("연락이 잘 돼요") || feedback.getFeedbackContent().equals("연락이 안 돼요")) && request.getCommunication() == null) {
                    feedback.setFeedbackType(null);
                } else if ((feedback.getFeedbackContent().equals("연락이 잘 돼요") || feedback.getFeedbackContent().equals("연락이 안 돼요")) && request.getCommunication()) {
                    feedback.setFeedbackContent("연락이 잘 돼요");
                    feedback.setFeedbackType(true);
                } else if ((feedback.getFeedbackContent().equals("연락이 잘 돼요") || feedback.getFeedbackContent().equals("연락이 안 돼요")) && request.getCommunication() == false) {
                    feedback.setFeedbackContent("연락이 안 돼요");
                    feedback.setFeedbackType(false);
                }

                if ((feedback.getFeedbackContent().equals("시간약속을 잘 지켜요") || feedback.getFeedbackContent().equals("시간약속을 잘 안지켜요")) && request.getPunctual() == null) {
                    feedback.setFeedbackType(null);
                } else if ((feedback.getFeedbackContent().equals("시간약속을 잘 지켜요") || feedback.getFeedbackContent().equals("시간약속을 잘 안지켜요")) && request.getPunctual()) {
                    feedback.setFeedbackContent("시간약속을 잘 지켜요");
                    feedback.setFeedbackType(true);
                } else if ((feedback.getFeedbackContent().equals("시간약속을 잘 지켜요") || feedback.getFeedbackContent().equals("시간약속을 잘 안지켜요")) && request.getPunctual() == false) {
                    feedback.setFeedbackContent("시간약속을 잘 안지켜요");
                    feedback.setFeedbackType(false);
                }

                if ((feedback.getFeedbackContent().equals("능력이 뛰어나요") || feedback.getFeedbackContent().equals("능력이 뒤떨어져요")) && request.getCompetent() == null) {
                    feedback.setFeedbackType(null);
                } else if ((feedback.getFeedbackContent().equals("능력이 뛰어나요") || feedback.getFeedbackContent().equals("능력이 뒤떨어져요")) && request.getCompetent()) {
                    feedback.setFeedbackContent("능력이 뛰어나요");
                    feedback.setFeedbackType(true);
                } else if ((feedback.getFeedbackContent().equals("능력이 뛰어나요") || feedback.getFeedbackContent().equals("능력이 뒤떨어져요")) && request.getCompetent() == false) {
                    feedback.setFeedbackContent("능력이 뒤떨어져요");
                    feedback.setFeedbackType(false);
                }

                if ((feedback.getFeedbackContent().equals("말을 조리있게 잘해요") || feedback.getFeedbackContent().equals("말을 조리있게 못해요")) && request.getArticulate() == null) {
                    feedback.setFeedbackType(null);
                } else if ((feedback.getFeedbackContent().equals("말을 조리있게 잘해요") || feedback.getFeedbackContent().equals("말을 조리있게 못해요")) && request.getArticulate()) {
                    feedback.setFeedbackContent("말을 조리있게 잘해요");
                    feedback.setFeedbackType(true);
                } else if ((feedback.getFeedbackContent().equals("말을 조리있게 잘해요") || feedback.getFeedbackContent().equals("말을 조리있게 못해요")) && request.getArticulate() == false) {
                    feedback.setFeedbackContent("말을 조리있게 못해요");
                    feedback.setFeedbackType(false);
                }

                if ((feedback.getFeedbackContent().equals("빈틈이 없어요") || feedback.getFeedbackContent().equals("빈틈이 있어요")) && request.getThorough() == null) {
                    feedback.setFeedbackType(null);
                } else if ((feedback.getFeedbackContent().equals("빈틈이 없어요") || feedback.getFeedbackContent().equals("빈틈이 있어요")) && request.getThorough()) {
                    feedback.setFeedbackContent("빈틈이 없어요");
                    feedback.setFeedbackType(true);
                } else if ((feedback.getFeedbackContent().equals("빈틈이 없어요") || feedback.getFeedbackContent().equals("빈틈이 있어요")) && request.getThorough() == false) {
                    feedback.setFeedbackContent("빈틈이 있어요");
                    feedback.setFeedbackType(false);
                }

                if ((feedback.getFeedbackContent().equals("재미있어요") || feedback.getFeedbackContent().equals("재미없어요")) && request.getEngaging() == null) {
                    feedback.setFeedbackType(null);
                } else if ((feedback.getFeedbackContent().equals("재미있어요") || feedback.getFeedbackContent().equals("재미없어요")) && request.getEngaging()) {
                    feedback.setFeedbackContent("재미있어요");
                    feedback.setFeedbackType(true);
                } else if ((feedback.getFeedbackContent().equals("재미있어요") || feedback.getFeedbackContent().equals("재미없어요")) && request.getEngaging() == false) {
                    feedback.setFeedbackContent("재미없어요");
                    feedback.setFeedbackType(false);
                }
            });
            feedbackRepository.saveAll(feedbackList);
            feedbackRegistrationRepository.save(feedbackRegistration);
        }
        return "피드백 등록(수정) 성공";
    }
}
