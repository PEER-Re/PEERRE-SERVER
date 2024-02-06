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

        if(!feedbackRegistrationRepository.existsByRecipientIdAndUserAndProject(teamMemberId, user, project)) {

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
                            .feedback_content(request.getCommunication() ? "연락이 잘 돼요" : "연락이 안 돼요")
                            .feedback_type(request.getCommunication())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                if (request.getPunctual() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedback_content(request.getPunctual() ? "시간약속을 잘 지켜요" : "시간약속을 잘 안지켜요")
                            .feedback_type(request.getPunctual())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                if (request.getCompetent() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedback_content(request.getCompetent() ? "능력이 뛰어나요" : "능력이 뒤떨어져요")
                            .feedback_type(request.getCompetent())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                if (request.getArticulate() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedback_content(request.getArticulate() ? "말을 조리있게 잘해요" : "말을 조리있게 못해요")
                            .feedback_type(request.getArticulate())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                if (request.getThorough() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedback_content(request.getThorough() ? "빈틈이 없어요" : "빈틈이 있어요")
                            .feedback_type(request.getThorough())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                if (request.getEngaging() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedback_content(request.getEngaging() ? "재미있어요" : "재미없어요")
                            .feedback_type(request.getEngaging())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                feedbackRepository.saveAll(feedbackList);
            }

            feedbackRegistrationRepository.save(feedbackRegistration);

            if (feedbackAggregation.getEvaluation_status() == null) {
                feedbackAggregation.setEvaluation_status(false);
            }

            if (!feedbackAggregation.getEvaluation_status()) {
                feedbackAggregation.setEvaluation_status(true);
            }
            feedbackAggregationRepository.save(feedbackAggregation);
        }

        else {
            FeedbackRegistration feedbackRegistration = feedbackRegistrationRepository.findByRecipientIdAndUserAndProject(teamMemberId, user, project);

            List<Feedback> feedbackList = feedbackRegistration.getFeedbackList();

            feedbackList
                    .forEach(feedback -> {
                        if((feedback.getFeedback_content().equals("연락이 잘 돼요") || feedback.getFeedback_content().equals("연락이 안 돼요")) && request.getCommunication()==null) {
                            feedback.setFeedback_type(null);
                        } else if((feedback.getFeedback_content().equals("연락이 잘 돼요") || feedback.getFeedback_content().equals("연락이 안 돼요")) && request.getCommunication()) {
                            feedback.setFeedback_content("연락이 잘 돼요");
                            feedback.setFeedback_type(true);
                        } else if((feedback.getFeedback_content().equals("연락이 잘 돼요") || feedback.getFeedback_content().equals("연락이 안 돼요")) && request.getCommunication()==false) {
                            feedback.setFeedback_content("연락이 안 돼요");
                            feedback.setFeedback_type(false);
                        }

                        if ((feedback.getFeedback_content().equals("시간약속을 잘 지켜요") || feedback.getFeedback_content().equals("시간약속을 잘 안지켜요")) && request.getPunctual()==null) {
                            feedback.setFeedback_type(null);
                        } else if((feedback.getFeedback_content().equals("시간약속을 잘 지켜요") || feedback.getFeedback_content().equals("시간약속을 잘 안지켜요")) && request.getPunctual()) {
                            feedback.setFeedback_content("시간약속을 잘 지켜요");
                            feedback.setFeedback_type(true);
                        } else if((feedback.getFeedback_content().equals("시간약속을 잘 지켜요") || feedback.getFeedback_content().equals("시간약속을 잘 안지켜요")) && request.getPunctual()==false) {
                            feedback.setFeedback_content("시간약속을 잘 안지켜요");
                            feedback.setFeedback_type(false);
                        }

                        if ((feedback.getFeedback_content().equals("능력이 뛰어나요") || feedback.getFeedback_content().equals("능력이 뒤떨어져요")) && request.getCompetent()==null) {
                            feedback.setFeedback_type(null);
                        } else if((feedback.getFeedback_content().equals("능력이 뛰어나요") || feedback.getFeedback_content().equals("능력이 뒤떨어져요")) && request.getCompetent()) {
                            feedback.setFeedback_content("능력이 뛰어나요");
                            feedback.setFeedback_type(true);
                        } else if((feedback.getFeedback_content().equals("능력이 뛰어나요") || feedback.getFeedback_content().equals("능력이 뒤떨어져요")) && request.getCompetent()==false)  {
                            feedback.setFeedback_content("능력이 뒤떨어져요");
                            feedback.setFeedback_type(false);
                        }

                        if((feedback.getFeedback_content().equals("말을 조리있게 잘해요")|| feedback.getFeedback_content().equals("말을 조리있게 못해요")) && request.getArticulate()==null) {
                            feedback.setFeedback_type(null);
                        } else if((feedback.getFeedback_content().equals("말을 조리있게 잘해요") || feedback.getFeedback_content().equals("말을 조리있게 못해요")) && request.getArticulate()) {
                            feedback.setFeedback_content("말을 조리있게 잘해요");
                            feedback.setFeedback_type(true);
                        } else if((feedback.getFeedback_content().equals("말을 조리있게 잘해요")|| feedback.getFeedback_content().equals("말을 조리있게 못해요")) && request.getArticulate()==false){
                            feedback.setFeedback_content("말을 조리있게 못해요");
                            feedback.setFeedback_type(false);
                        }

                        if((feedback.getFeedback_content().equals("빈틈이 없어요") || feedback.getFeedback_content().equals("빈틈이 있어요")) && request.getThorough()==null) {
                            feedback.setFeedback_type(null);
                        } else if((feedback.getFeedback_content().equals("빈틈이 없어요") || feedback.getFeedback_content().equals("빈틈이 있어요")) && request.getThorough()) {
                            feedback.setFeedback_content("빈틈이 없어요");
                            feedback.setFeedback_type(true);
                        } else if((feedback.getFeedback_content().equals("빈틈이 없어요") || feedback.getFeedback_content().equals("빈틈이 있어요")) && request.getThorough()==false) {
                            feedback.setFeedback_content("빈틈이 있어요");
                            feedback.setFeedback_type(false);
                        }

                        if((feedback.getFeedback_content().equals("재미있어요") ||feedback.getFeedback_content().equals("재미없어요")) && request.getEngaging()==null) {
                            feedback.setFeedback_type(null);
                        } else if((feedback.getFeedback_content().equals("재미있어요") || feedback.getFeedback_content().equals("재미없어요")) && request.getEngaging()) {
                            feedback.setFeedback_content("재미있어요");
                            feedback.setFeedback_type(true);
                        }  else if((feedback.getFeedback_content().equals("재미있어요") ||feedback.getFeedback_content().equals("재미없어요")) && request.getEngaging()==false)  {
                            feedback.setFeedback_content("재미없어요");
                            feedback.setFeedback_type(false);
                        }
                    });
            feedbackRepository.saveAll(feedbackList);
            feedbackRegistrationRepository.save(feedbackRegistration);

        }
        return "피드백 등록(수정) 성공";
    }
}
