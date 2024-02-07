package org.umc.peerre.domain.feedback.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.umc.peerre.domain.feedback.dto.request.FeedbackRequest;
import org.umc.peerre.domain.feedback.dto.response.FeedbackResponse;
import org.umc.peerre.domain.feedback.entity.Feedback;
import org.umc.peerre.domain.feedback.entity.FeedbackAggregation;
import org.umc.peerre.domain.feedback.entity.FeedbackRegistration;
import org.umc.peerre.domain.feedback.repository.FeedbackAggregationRepository;
import org.umc.peerre.domain.feedback.repository.FeedbackRegistrationRepository;
import org.umc.peerre.domain.feedback.repository.FeedbackRepository;
import org.umc.peerre.domain.project.constant.Status;
import org.umc.peerre.domain.project.entity.Project;
import org.umc.peerre.domain.project.repository.ProjectRepository;
import org.umc.peerre.domain.teamspace.entity.Teamspace;
import org.umc.peerre.domain.teamspace.entity.UserTeamspace;
import org.umc.peerre.domain.teamspace.repository.TeamspaceRepository;
import org.umc.peerre.domain.teamspace.repository.UserTeamspaceRepository;
import org.umc.peerre.domain.user.entity.User;
import org.umc.peerre.domain.user.repository.UserRepository;

import java.util.*;
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
    private final UserTeamspaceRepository userTeamspaceRepository;
    private final TeamspaceRepository teamspaceRepository;

    public String enrollFeedback(Long userId, Long teamMemberId, Long projectId, FeedbackRequest.Feedback request) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당하는 유저가 없습니다."));
        userRepository.findById(teamMemberId).orElseThrow(() -> new EntityNotFoundException("해당하는 팀원이 없습니다."));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("해당하는 프로젝트가 없습니다."));

        if(project.getStatus()==Status.종료) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"진행중인 프로젝트가 아닙니다.");
        }

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

            if (feedbackAggregation.getEvaluationStatus() == null) {
                feedbackAggregation.setEvaluationStatus(false);
            }

            if (!feedbackAggregation.getEvaluationStatus()) {
                feedbackAggregation.setEvaluationStatus(true);
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

    public FeedbackResponse.myReportResponse getMyReport(Long userId,Long projectId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저가 없습니다."));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 프로젝트가 없습니다."));

        if(project.getStatus()==Status.종료) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"진행중인 프로젝트가 아닙니다.");
        }

        Long teamId = project.getTeamspace().getId();

        Teamspace teamspace = teamspaceRepository.findById(teamId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 팀스페이스가 없습니다."));

        FeedbackResponse.UserInfo userInfo = FeedbackResponse.UserInfo.builder()
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImg_url())
                .projectName(project.getTitle())
                .teamName(teamspace.getName())
                .teamProfile(teamspace.getProfile())
                .build();

        FeedbackResponse.YesFeedbackInfo yesFeedbackInfo = FeedbackResponse.YesFeedbackInfo.builder()
                .goodArticulate("말을 조리있게 잘해요")
                .goodCompetent("능력이 뛰어나요")
                .goodEngaging("재미있어요")
                .goodPunctual("시간약속을 잘 지켜요")
                .goodCommunication("연락이 잘 돼요")
                .goodThorough("빈틈이 없어요")
                .goodArticulateNum(0)
                .goodCompetentNum(0)
                .goodCommunicationNum(0)
                .goodEngagingNum(0)
                .goodPunctualNum(0)
                .goodThoroughNum(0)
                .build();

        FeedbackResponse.NoFeedbackInfo noFeedbackInfo = FeedbackResponse.NoFeedbackInfo.builder()
                .badArticulate("말을 조리있게 못해요")
                .badCommunication("연락이 안 돼요")
                .badCompetent("능력이 뒤떨어져요")
                .badEngaging("재미없어요")
                .badPunctual("시간약속을 잘 안지켜요")
                .badThorough("빈틈이 있어요")
                .badArticulateNum(0)
                .badCompetentNum(0)
                .badCommunicationNum(0)
                .badEngagingNum(0)
                .badPunctualNum(0)
                .badThoroughNum(0)
                .build();

        Optional<List<FeedbackRegistration>> feedbackRegistration = feedbackRegistrationRepository.findByRecipientIdAndProject(userId, project);

        if(feedbackRegistration.isPresent()) {

            List<FeedbackRegistration> feedbackRegistrations = feedbackRegistration.get();
            for (FeedbackRegistration signleFeedbackRegistration : feedbackRegistrations) {
                List<Feedback> feedbackList = signleFeedbackRegistration.getFeedbackList();

                for (Feedback feedback : feedbackList) {
                    if (feedback.getFeedback_content().equals("말을 조리있게 잘해요") && feedback.getFeedback_type()) {
                        yesFeedbackInfo.setGoodArticulateNum(yesFeedbackInfo.getGoodArticulateNum() + 1);
                    } else if (feedback.getFeedback_content().equals("말을 조리있게 못해요") && !feedback.getFeedback_type()) {
                        noFeedbackInfo.setBadArticulateNum(noFeedbackInfo.getBadArticulateNum() + 1);
                    }

                    if (feedback.getFeedback_content().equals("능력이 뛰어나요") && feedback.getFeedback_type()) {
                        yesFeedbackInfo.setGoodCompetentNum(yesFeedbackInfo.getGoodCompetentNum() + 1);
                    } else if (feedback.getFeedback_content().equals("능력이 뒤떨어져요") && !feedback.getFeedback_type()) {
                        noFeedbackInfo.setBadCompetentNum(noFeedbackInfo.getBadCompetentNum() + 1);
                    }

                    if (feedback.getFeedback_content().equals("재미있어요") && feedback.getFeedback_type()) {
                        yesFeedbackInfo.setGoodEngagingNum(yesFeedbackInfo.getGoodEngagingNum() + 1);
                    } else if (feedback.getFeedback_content().equals("재미없어요") && !feedback.getFeedback_type()) {
                        noFeedbackInfo.setBadEngagingNum(noFeedbackInfo.getBadEngagingNum() + 1);
                    }

                    if (feedback.getFeedback_content().equals("시간약속을 잘 지켜요") && feedback.getFeedback_type()) {
                        yesFeedbackInfo.setGoodPunctualNum(yesFeedbackInfo.getGoodPunctualNum() + 1);
                    } else if (feedback.getFeedback_content().equals("시간약속을 잘 안지켜요") && !feedback.getFeedback_type()) {
                        noFeedbackInfo.setBadPunctualNum(noFeedbackInfo.getBadPunctualNum() + 1);
                    }

                    if (feedback.getFeedback_content().equals("연락이 잘 돼요") && feedback.getFeedback_type()) {
                        yesFeedbackInfo.setGoodCommunicationNum(yesFeedbackInfo.getGoodCommunicationNum() + 1);
                    } else if (feedback.getFeedback_content().equals("연락이 안 돼요") && !feedback.getFeedback_type()) {
                        noFeedbackInfo.setBadCommunicationNum(noFeedbackInfo.getBadCommunicationNum() + 1);
                    }

                    if (feedback.getFeedback_content().equals("빈틈이 없어요") && feedback.getFeedback_type()) {
                        yesFeedbackInfo.setGoodThoroughNum(yesFeedbackInfo.getGoodThoroughNum() + 1);
                    } else if (feedback.getFeedback_content().equals("빈틈이 있어요") && !feedback.getFeedback_type()) {
                        noFeedbackInfo.setBadThoroughNum(noFeedbackInfo.getBadThoroughNum() + 1);
                    }
                }
            }
        }

        return FeedbackResponse.myReportResponse.builder()
                .userInfo(userInfo)
                .noFeedbackInfo(noFeedbackInfo)
                .yesFeedbackInfo(yesFeedbackInfo)
                .totalEvaluationNum(teamspace.getSize())
                .build();
    }

    public FeedbackResponse.TeamReportResponse getTeamReport(Long userId, Long projectId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저가 없습니다."));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 프로젝트가 없습니다."));

        if(project.getStatus()==Status.종료) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"진행중인 프로젝트가 아닙니다.");
        }

        Long teamId = project.getTeamspace().getId();

        Teamspace teamspace = teamspaceRepository.findById(teamId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 팀스페이스가 없습니다."));

        FeedbackResponse.TeamInfo teamInfo = FeedbackResponse.TeamInfo.builder()
                .teamName(teamspace.getName())
                .teamProfile(teamspace.getProfile())
                .projectName(project.getTitle())
                .build();

        List<UserTeamspace> userTeamspaces = userTeamspaceRepository.findByTeamspace(teamspace)
                .orElseThrow(()->new EntityNotFoundException("해당하는 팀에 속해있는 유저가 없습니다."));

        List<String> teamUserNames = new ArrayList<>();
        for (UserTeamspace userTeamspace : userTeamspaces) {
            teamUserNames.add(userTeamspace.getUser().getNickname());
        }
        teamInfo.setTeamUserNames(teamUserNames);

        List<Long> userIds = userTeamspaces.stream()
                .map(userTeamspace -> userTeamspace.getUser().getId())
                .toList();

        List<Integer> evaluationStatusTrueCounts = new ArrayList<>();
        for (Long id : userIds) {
            Integer count = feedbackAggregationRepository.countByProjectIdAndUserIdAndEvaluationStatus(projectId, id, true);
            evaluationStatusTrueCounts.add(count);
        }

        Integer totalEvaluationCount = evaluationStatusTrueCounts.stream()
                .mapToInt(Integer::intValue)
                .sum();

        if(teamspace.getSize()==null)
        {
            teamInfo.setEvaluationProgress(0);
        } else {
            Integer evaluationProgress = (int) Math.round((double) totalEvaluationCount / teamspace.getSize() * 100);
            teamInfo.setEvaluationProgress(evaluationProgress);
        }

        Map<Long, FeedbackResponse.TeamFeedbackInfo> teamFeedbackInfoMap = new HashMap<>();
        for (Long id : userIds) {

            Long teamUserId = id;
            // FeedbackRegistration 조회
            List<FeedbackRegistration> feedbackRegistrations = feedbackRegistrationRepository.findByRecipientIdAndProject(teamUserId, project)
                    .orElseThrow(() -> new EntityNotFoundException("해당하는 피드백 등록이 없습니다."));

            for (FeedbackRegistration feedbackRegistration : feedbackRegistrations) {
                Long recipientId = feedbackRegistration.getRecipientId();
                FeedbackResponse.TeamFeedbackInfo teamFeedbackInfo = teamFeedbackInfoMap.getOrDefault(recipientId, FeedbackResponse.TeamFeedbackInfo.builder()
                        .yesFeedbackNum(0)
                        .goodFeedbackContent(new HashSet<>())
                        .build());

                // FeedbackList에서 yes 피드백 개수 계산
                Integer yesFeedbackNum = (int) feedbackRegistration.getFeedbackList().stream()
                        .filter(Feedback::getFeedback_type)
                        .count();

                teamFeedbackInfo.setYesFeedbackNum(teamFeedbackInfo.getYesFeedbackNum() + yesFeedbackNum);

                // goodFeedbackContent 추가
                Set<String> goodFeedbackContent = teamFeedbackInfo.getGoodFeedbackContent();
                goodFeedbackContent.addAll(feedbackRegistration.getFeedbackList().stream()
                        .filter(feedback -> feedback.getFeedback_type() && feedback.getFeedback_content() != null)
                        .map(Feedback::getFeedback_content)
                        .collect(Collectors.toSet()));
                teamFeedbackInfo.setGoodFeedbackContent(goodFeedbackContent);

                // Map에 저장
                teamFeedbackInfoMap.put(recipientId, teamFeedbackInfo);
            }
        }

        // rank 계산
        List<FeedbackResponse.TeamFeedbackInfo> teamFeedbackInfoList = new ArrayList<>(teamFeedbackInfoMap.values());
        teamFeedbackInfoList.sort(Comparator.comparingInt(FeedbackResponse.TeamFeedbackInfo::getYesFeedbackNum).reversed());
        for (int i = 0; i < teamFeedbackInfoList.size(); i++) {
            teamFeedbackInfoList.get(i).setRank(i + 1);
        }
        return FeedbackResponse.TeamReportResponse.builder()
                .teamInfo(teamInfo)
                .teamFeedbackInfoList(teamFeedbackInfoList)
                .build();
    }
}
