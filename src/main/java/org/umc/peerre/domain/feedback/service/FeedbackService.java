package org.umc.peerre.domain.feedback.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
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

import java.time.LocalDateTime;
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
        User teamMember = userRepository.findById(teamMemberId).orElseThrow(() -> new EntityNotFoundException("해당하는 팀원이 없습니다."));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("해당하는 프로젝트가 없습니다."));

        if (project.getStatus() == Status.종료) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "진행중인 프로젝트가 아닙니다.");
        }

        // 팀 멤버의 피드백 집계 테이블
        FeedbackAggregation teamMemberFeedbackAggregation = feedbackAggregationRepository.findByUserAndProject(teamMember, project)
                .orElseGet(() -> FeedbackAggregation.builder()
                        .yesFeedbackCount(0)
                        .noFeedbackCount(0)
                        .user(teamMember)
                        .project(project)
                        .build());

        if (!feedbackRegistrationRepository.existsByRecipientIdAndUserAndProject(teamMemberId, user, project)) {


            FeedbackRegistration feedbackRegistration = FeedbackRegistration.builder()
                    .recipientId(teamMemberId)
                    .user(user)
                    .project(project)
                    .build();

            FeedbackAggregation feedbackAggregation = feedbackAggregationRepository.findByUserAndProject(user, project)
                    .orElseGet(() -> FeedbackAggregation.builder()
                            .user(user)
                            .project(project)
                            .build());

            if (request != null) {
                List<Feedback> feedbackList = new ArrayList<>();

                if (request.getCommunication() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedbackContent(request.getCommunication() ? "프로젝트 관련 소통이 원활함" : "프로젝트 관련 소통이 원활하지 않음")
                            .feedbackType(request.getCommunication())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                if (request.getPunctual() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedbackContent(request.getPunctual() ? "정해진 기한 내 업무를 달성함" : "정해진 기한 내 업무를 달성하지 못함")
                            .feedbackType(request.getPunctual())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                if (request.getCompetent() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedbackContent(request.getCompetent() ? "업무 달성 결과의 질적 수준이 높음" : "업무 달성 결과의 질적 수준이 낮음")
                            .feedbackType(request.getCompetent())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                if (request.getArticulate() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedbackContent(request.getArticulate() ? "동료의 의견을 적극 수용함" : "피드백 수용 및 적용에 어려움이 있음")
                            .feedbackType(request.getArticulate())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                if (request.getThorough() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedbackContent(request.getThorough() ? "업무를 책임감 있게 수행함" : "업무에 대한 책임감이 필요함")
                            .feedbackType(request.getThorough())
                            .feedbackRegistration(feedbackRegistration)
                            .build();
                    feedbackList.add(feedback);
                }

                if (request.getEngaging() != null) {
                    Feedback feedback = Feedback.builder()
                            .feedbackContent(request.getEngaging() ? "프로젝트에 적극적인 자세로 참여함" : "프로젝트에 참여하는 자세가 수동적임")
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

            // 피드백 등록 시에 팀원의 yes피드백 개수, no 피드백 개수 저장
            assert request != null;
            long teamMemberYesFeedbackCount= Stream.of(
                            request.getCommunication(),
                            request.getPunctual(),
                            request.getCompetent(),
                            request.getArticulate(),
                            request.getThorough(),
                            request.getEngaging())
                    .filter(Boolean.TRUE::equals)
                    .count();

            long teamMemberNoFeedbackCount= Stream.of(
                            request.getCommunication(),
                            request.getPunctual(),
                            request.getCompetent(),
                            request.getArticulate(),
                            request.getThorough(),
                            request.getEngaging())
                    .filter(Boolean.FALSE::equals)
                    .count();

            Integer yesFeedbackNum = (int)teamMemberYesFeedbackCount;
            Integer noFeedbackNum = (int)teamMemberNoFeedbackCount;

            teamMemberFeedbackAggregation.setYesFeedbackCount(yesFeedbackNum);
            teamMemberFeedbackAggregation.setNoFeedbackCount(noFeedbackNum);
            feedbackAggregationRepository.save(teamMemberFeedbackAggregation);

        }
        // 피드백 수정
        else {
            FeedbackRegistration feedbackRegistration = feedbackRegistrationRepository.findByRecipientIdAndUserAndProject(teamMemberId, user, project)
                    .orElseThrow(() -> new EntityNotFoundException("해당하는 피드백 등록이 없습니다."));

            List<Feedback> feedbackList = feedbackRegistration.getFeedbackList();

            // true 개수 찾기
            long previousYesFeedbackNum = feedbackList.stream()
                    .filter(feedback -> Boolean.TRUE.equals(Optional.ofNullable(feedback.getFeedbackType()).orElse(false)))
                    .count();

            // false 개수 찾기
            long previousNoFeedbackCount = feedbackList.stream()
                    .filter(feedback -> Boolean.FALSE.equals(Optional.ofNullable(feedback.getFeedbackType()).orElse(true)))
                    .count();
            // request의 true, false 개수 count
            long teamMemberYesFeedbackCount= Stream.of(
                            request.getCommunication(),
                            request.getPunctual(),
                            request.getCompetent(),
                            request.getArticulate(),
                            request.getThorough(),
                            request.getEngaging())
                    .filter(Boolean.TRUE::equals)
                    .count();

            long teamMemberNoFeedbackCount= Stream.of(
                            request.getCommunication(),
                            request.getPunctual(),
                            request.getCompetent(),
                            request.getArticulate(),
                            request.getThorough(),
                            request.getEngaging())
                    .filter(Boolean.FALSE::equals)
                    .count();

            feedbackList.forEach(feedback -> {
                if ((feedback.getFeedbackContent().equals("프로젝트 관련 소통이 원활함") || feedback.getFeedbackContent().equals("프로젝트 관련 소통이 원활하지 않음")) && request.getCommunication() == null) {
                    feedback.setFeedbackType(null);
                } else if ((feedback.getFeedbackContent().equals("프로젝트 관련 소통이 원활함") || feedback.getFeedbackContent().equals("프로젝트 관련 소통이 원활하지 않음")) && request.getCommunication()) {
                    feedback.setFeedbackContent("프로젝트 관련 소통이 원활함");
                    feedback.setFeedbackType(true);
                } else if ((feedback.getFeedbackContent().equals("프로젝트 관련 소통이 원활함") || feedback.getFeedbackContent().equals("프로젝트 관련 소통이 원활하지 않음")) && request.getCommunication() == false) {
                    feedback.setFeedbackContent("프로젝트 관련 소통이 원활하지 않음");
                    feedback.setFeedbackType(false);
                }

                if ((feedback.getFeedbackContent().equals("정해진 기한 내 업무를 달성함") || feedback.getFeedbackContent().equals("정해진 기한 내 업무를 달성하지 못함")) && request.getPunctual() == null) {
                    feedback.setFeedbackType(null);
                } else if ((feedback.getFeedbackContent().equals("정해진 기한 내 업무를 달성함") || feedback.getFeedbackContent().equals("정해진 기한 내 업무를 달성하지 못함")) && request.getPunctual()) {
                    feedback.setFeedbackContent("정해진 기한 내 업무를 달성함");
                    feedback.setFeedbackType(true);
                } else if ((feedback.getFeedbackContent().equals("정해진 기한 내 업무를 달성함") || feedback.getFeedbackContent().equals("정해진 기한 내 업무를 달성하지 못함")) && request.getPunctual() == false) {
                    feedback.setFeedbackContent("정해진 기한 내 업무를 달성하지 못함");
                    feedback.setFeedbackType(false);
                }

                if ((feedback.getFeedbackContent().equals("업무 달성 결과의 질적 수준이 높음") || feedback.getFeedbackContent().equals("업무 달성 결과의 질적 수준이 낮음")) && request.getCompetent() == null) {
                    feedback.setFeedbackType(null);
                } else if ((feedback.getFeedbackContent().equals("업무 달성 결과의 질적 수준이 높음") || feedback.getFeedbackContent().equals("업무 달성 결과의 질적 수준이 낮음")) && request.getCompetent()) {
                    feedback.setFeedbackContent("업무 달성 결과의 질적 수준이 높음");
                    feedback.setFeedbackType(true);
                } else if ((feedback.getFeedbackContent().equals("업무 달성 결과의 질적 수준이 높음") || feedback.getFeedbackContent().equals("업무 달성 결과의 질적 수준이 낮음")) && request.getCompetent() == false) {
                    feedback.setFeedbackContent("업무 달성 결과의 질적 수준이 낮음");
                    feedback.setFeedbackType(false);
                }

                if ((feedback.getFeedbackContent().equals("동료의 의견을 적극 수용함") || feedback.getFeedbackContent().equals("피드백 수용 및 적용에 어려움이 있음")) && request.getArticulate() == null) {
                    feedback.setFeedbackType(null);
                } else if ((feedback.getFeedbackContent().equals("동료의 의견을 적극 수용함") || feedback.getFeedbackContent().equals("피드백 수용 및 적용에 어려움이 있음")) && request.getArticulate()) {
                    feedback.setFeedbackContent("동료의 의견을 적극 수용함");
                    feedback.setFeedbackType(true);
                } else if ((feedback.getFeedbackContent().equals("동료의 의견을 적극 수용함") || feedback.getFeedbackContent().equals("피드백 수용 및 적용에 어려움이 있음")) && request.getArticulate() == false) {
                    feedback.setFeedbackContent("피드백 수용 및 적용에 어려움이 있음");
                    feedback.setFeedbackType(false);
                }

                if ((feedback.getFeedbackContent().equals("업무를 책임감 있게 수행함") || feedback.getFeedbackContent().equals("업무에 대한 책임감이 필요함")) && request.getThorough() == null) {
                    feedback.setFeedbackType(null);
                } else if ((feedback.getFeedbackContent().equals("업무를 책임감 있게 수행함") || feedback.getFeedbackContent().equals("업무에 대한 책임감이 필요함")) && request.getThorough()) {
                    feedback.setFeedbackContent("업무를 책임감 있게 수행함");
                    feedback.setFeedbackType(true);
                } else if ((feedback.getFeedbackContent().equals("업무를 책임감 있게 수행함") || feedback.getFeedbackContent().equals("업무에 대한 책임감이 필요함")) && request.getThorough() == false) {
                    feedback.setFeedbackContent("업무에 대한 책임감이 필요함");
                    feedback.setFeedbackType(false);
                }

                if ((feedback.getFeedbackContent().equals("프로젝트에 적극적인 자세로 참여함") || feedback.getFeedbackContent().equals("프로젝트에 참여하는 자세가 수동적임")) && request.getEngaging() == null) {
                    feedback.setFeedbackType(null);
                } else if ((feedback.getFeedbackContent().equals("프로젝트에 적극적인 자세로 참여함") || feedback.getFeedbackContent().equals("프로젝트에 참여하는 자세가 수동적임")) && request.getEngaging()) {
                    feedback.setFeedbackContent("프로젝트에 적극적인 자세로 참여함");
                    feedback.setFeedbackType(true);
                } else if ((feedback.getFeedbackContent().equals("프로젝트에 적극적인 자세로 참여함") || feedback.getFeedbackContent().equals("프로젝트에 참여하는 자세가 수동적임")) && request.getEngaging() == false) {
                    feedback.setFeedbackContent("프로젝트에 참여하는 자세가 수동적임");
                    feedback.setFeedbackType(false);
                }
            });

            // 피드백 등록 시에 팀원의 yes피드백 개수, no 피드백 개수 저장
            Integer minusYesFeedbackNum = (int) previousYesFeedbackNum;
            Integer minusNoFeedbackNum = (int) previousNoFeedbackCount;
            Integer yesFeedbackNum = (int)teamMemberYesFeedbackCount;
            Integer noFeedbackNum = (int)teamMemberNoFeedbackCount;

            teamMemberFeedbackAggregation.setYesFeedbackCount(teamMemberFeedbackAggregation.getYesFeedbackCount()-minusYesFeedbackNum+yesFeedbackNum);
            teamMemberFeedbackAggregation.setNoFeedbackCount(teamMemberFeedbackAggregation.getNoFeedbackCount()-minusNoFeedbackNum+noFeedbackNum);
            feedbackAggregationRepository.save(teamMemberFeedbackAggregation);
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
                .profileImageUrl(user.getProfileImgUrl())
                .projectName(project.getTitle())
                .teamName(teamspace.getName())
                .teamProfile(teamspace.getProfile())
                .build();

        FeedbackResponse.YesFeedbackInfo yesFeedbackInfo = FeedbackResponse.YesFeedbackInfo.builder()
                .goodArticulate("동료의 의견을 적극 수용함")
                .goodCompetent("업무 달성 결과의 질적 수준이 높음")
                .goodEngaging("프로젝트에 적극적인 자세로 참여함")
                .goodPunctual("정해진 기한 내 업무를 달성함")
                .goodCommunication("프로젝트 관련 소통이 원활함")
                .goodThorough("업무를 책임감 있게 수행함")
                .goodArticulateNum(0)
                .goodCompetentNum(0)
                .goodCommunicationNum(0)
                .goodEngagingNum(0)
                .goodPunctualNum(0)
                .goodThoroughNum(0)
                .build();

        FeedbackResponse.NoFeedbackInfo noFeedbackInfo = FeedbackResponse.NoFeedbackInfo.builder()
                .badArticulate("피드백 수용 및 적용에 어려움이 있음")
                .badCommunication("프로젝트 관련 소통이 원활하지 않음")
                .badCompetent("업무 달성 결과의 질적 수준이 낮음")
                .badEngaging("프로젝트에 참여하는 자세가 수동적임")
                .badPunctual("정해진 기한 내 업무를 달성하지 못함")
                .badThorough("업무에 대한 책임감이 필요함")
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
                    if(feedback.getFeedbackType()!=null) {
                        if (feedback.getFeedbackContent().equals("동료의 의견을 적극 수용함") && feedback.getFeedbackType()) {
                            yesFeedbackInfo.setGoodArticulateNum(yesFeedbackInfo.getGoodArticulateNum() + 1);
                        } else if (feedback.getFeedbackContent().equals("피드백 수용 및 적용에 어려움이 있음") && !feedback.getFeedbackType()) {
                            noFeedbackInfo.setBadArticulateNum(noFeedbackInfo.getBadArticulateNum() + 1);
                        }

                        if (feedback.getFeedbackContent().equals("업무 달성 결과의 질적 수준이 높음") && feedback.getFeedbackType()) {
                            yesFeedbackInfo.setGoodCompetentNum(yesFeedbackInfo.getGoodCompetentNum() + 1);
                        } else if (feedback.getFeedbackContent().equals("업무 달성 결과의 질적 수준이 낮음") && !feedback.getFeedbackType()) {
                            noFeedbackInfo.setBadCompetentNum(noFeedbackInfo.getBadCompetentNum() + 1);
                        }

                        if (feedback.getFeedbackContent().equals("프로젝트에 적극적인 자세로 참여함") && feedback.getFeedbackType()) {
                            yesFeedbackInfo.setGoodEngagingNum(yesFeedbackInfo.getGoodEngagingNum() + 1);
                        } else if (feedback.getFeedbackContent().equals("프로젝트에 참여하는 자세가 수동적임") && !feedback.getFeedbackType()) {
                            noFeedbackInfo.setBadEngagingNum(noFeedbackInfo.getBadEngagingNum() + 1);
                        }

                        if (feedback.getFeedbackContent().equals("정해진 기한 내 업무를 달성함") && feedback.getFeedbackType()) {
                            yesFeedbackInfo.setGoodPunctualNum(yesFeedbackInfo.getGoodPunctualNum() + 1);
                        } else if (feedback.getFeedbackContent().equals("정해진 기한 내 업무를 달성하지 못함") && !feedback.getFeedbackType()) {
                            noFeedbackInfo.setBadPunctualNum(noFeedbackInfo.getBadPunctualNum() + 1);
                        }

                        if (feedback.getFeedbackContent().equals("프로젝트 관련 소통이 원활함") && feedback.getFeedbackType()) {
                            yesFeedbackInfo.setGoodCommunicationNum(yesFeedbackInfo.getGoodCommunicationNum() + 1);
                        } else if (feedback.getFeedbackContent().equals("프로젝트 관련 소통이 원활하지 않음") && !feedback.getFeedbackType()) {
                            noFeedbackInfo.setBadCommunicationNum(noFeedbackInfo.getBadCommunicationNum() + 1);
                        }

                        if (feedback.getFeedbackContent().equals("업무를 책임감 있게 수행함") && feedback.getFeedbackType()) {
                            yesFeedbackInfo.setGoodThoroughNum(yesFeedbackInfo.getGoodThoroughNum() + 1);
                        } else if (feedback.getFeedbackContent().equals("업무에 대한 책임감이 필요함") && !feedback.getFeedbackType()) {
                            noFeedbackInfo.setBadThoroughNum(noFeedbackInfo.getBadThoroughNum() + 1);
                        }
                    }
                }
            }
        }

        return FeedbackResponse.myReportResponse.builder()
                .userInfo(userInfo)
                .noFeedbackInfo(noFeedbackInfo)
                .yesFeedbackInfo(yesFeedbackInfo)
                .totalEvaluationNum(teamspace.getSize())
                .lastModifiedDate(LocalDateTime.now())
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
                    .orElse(new ArrayList<>());

            if(feedbackRegistrations.isEmpty()) {
                FeedbackResponse.TeamFeedbackInfo teamFeedbackInfo = FeedbackResponse.TeamFeedbackInfo.builder()
                        .rank(teamspace.getSize())
                        .yesFeedbackNum(0)
                        .goodFeedbackContent(new HashSet<>())
                        .build();
                teamFeedbackInfoMap.put(teamUserId, teamFeedbackInfo);
            } else {
                for (FeedbackRegistration feedbackRegistration : feedbackRegistrations) {
                    Long recipientId = feedbackRegistration.getRecipientId();
                    FeedbackResponse.TeamFeedbackInfo teamFeedbackInfo = teamFeedbackInfoMap.getOrDefault(recipientId, FeedbackResponse.TeamFeedbackInfo.builder()
                            .yesFeedbackNum(0)
                            .goodFeedbackContent(new HashSet<>())
                            .build());

                    // FeedbackList에서 yes 피드백 개수 계산
                    Integer yesFeedbackNum = (int) feedbackRegistration.getFeedbackList().stream()
                            .filter(feedback -> Boolean.TRUE.equals(Optional.ofNullable(feedback.getFeedbackType()).orElse(false)))
                            .count();

                    teamFeedbackInfo.setYesFeedbackNum(teamFeedbackInfo.getYesFeedbackNum() + yesFeedbackNum);

                    // goodFeedbackContent 추가
                    Set<String> goodFeedbackContent = teamFeedbackInfo.getGoodFeedbackContent();
                    goodFeedbackContent.addAll(feedbackRegistration.getFeedbackList().stream()
                            .filter(feedback -> Boolean.TRUE.equals(Optional.ofNullable(feedback.getFeedbackType()).orElse(false)) && feedback.getFeedbackContent() != null)
                            .map(Feedback::getFeedbackContent)
                            .collect(Collectors.toSet()));
                    teamFeedbackInfo.setGoodFeedbackContent(goodFeedbackContent);

                    // Map에 저장
                    teamFeedbackInfoMap.put(recipientId, teamFeedbackInfo);
                }
            }
        }

        // rank 계산
        List<FeedbackResponse.TeamFeedbackInfo> teamFeedbackInfoList = new ArrayList<>(teamFeedbackInfoMap.values());
        teamFeedbackInfoList.sort(Comparator.comparingInt(FeedbackResponse.TeamFeedbackInfo::getYesFeedbackNum).reversed());

        int rank = 1;
        int prevYesFeedbackNum = -1;
        int sameRankCount = 0;
        for (FeedbackResponse.TeamFeedbackInfo teamFeedbackInfo : teamFeedbackInfoList) {
            int currentYesFeedbackNum = teamFeedbackInfo.getYesFeedbackNum();
            if (currentYesFeedbackNum < prevYesFeedbackNum) {
                rank += sameRankCount;
                sameRankCount = 1;
            } else {
                sameRankCount++;
            }
            teamFeedbackInfo.setRank(rank);
            prevYesFeedbackNum = currentYesFeedbackNum;
        }

        return FeedbackResponse.TeamReportResponse.builder()
                .teamInfo(teamInfo)
                .teamFeedbackInfoList(teamFeedbackInfoList)
                .build();
    }

    public FeedbackResponse.SentFeedbackResponse getSentFeedbacks(Long userId, Long projectId) {

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

        List<UserTeamspace> userTeamspaces = userTeamspaceRepository.findByTeamspace(teamspace)
                .orElseThrow(()->new EntityNotFoundException("해당하는 팀에 속해있는 유저가 없습니다."));

        List<Long> teamUserIds = userTeamspaces.stream()
                .map(userTeamspace -> userTeamspace.getUser().getId())
                .toList();


        List<FeedbackResponse.SentFeedback> sentFeedbackList = new ArrayList<>();
        for (Long teamUserId : teamUserIds) {
            User teamUser = userRepository.findById(teamUserId).orElseThrow(() -> new RuntimeException("해당하는 팀원이 없습니다."));
            Optional<FeedbackRegistration> feedbackRegistrations = feedbackRegistrationRepository.findByRecipientIdAndUserAndProject(teamUserId, user, project);

            FeedbackResponse.SentFeedback sentFeedback;
            if (feedbackRegistrations.isPresent()) {
                FeedbackRegistration feedbackRegistration = feedbackRegistrations.get();
                sentFeedback = FeedbackResponse.SentFeedback.builder()
                        .teamName(teamspace.getName())
                        .teamUserProfileImageUrl(teamUser.getProfileImgUrl())
                        .teamUserNickname(teamUser.getNickname())
                        .yesFeedbackList(new ArrayList<>())
                        .noFeedbackList(new ArrayList<>())
                        .build();

                for (Feedback feedback : feedbackRegistration.getFeedbackList()) {
                    if (feedback.getFeedbackType() != null) {
                        if (feedback.getFeedbackType()) {
                            sentFeedback.getYesFeedbackList().add(feedback.getFeedbackContent());
                        } else {
                            sentFeedback.getNoFeedbackList().add(feedback.getFeedbackContent());
                        }
                    }
                }
            } else {
                sentFeedback = FeedbackResponse.SentFeedback.builder()
                        .teamName(teamspace.getName())
                        .teamUserProfileImageUrl(teamUser.getProfileImgUrl())
                        .teamUserNickname(teamUser.getNickname())
                        .yesFeedbackList(Collections.emptyList())
                        .noFeedbackList(Collections.emptyList())
                        .build();
            }

            sentFeedbackList.add(sentFeedback);
        }

        return FeedbackResponse.SentFeedbackResponse.builder()
                .sentFeedbackList(sentFeedbackList)
                .build();
    }
}
