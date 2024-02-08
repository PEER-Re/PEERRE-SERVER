package org.umc.peerre.domain.feedback.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.umc.peerre.domain.feedback.dto.request.FeedbackRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class FeedbackResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class YesFeedbackInfo {
        private String goodCommunication;
        private Integer goodCommunicationNum;

        private String goodPunctual;
        private Integer goodPunctualNum;

        private String goodCompetent;
        private Integer goodCompetentNum;

        private String goodArticulate;
        private Integer goodArticulateNum;

        private String goodThorough;
        private Integer goodThoroughNum;

        private String goodEngaging;
        private Integer goodEngagingNum;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoFeedbackInfo {
        private String badCommunication;
        private Integer badCommunicationNum;

        private String badPunctual;
        private Integer badPunctualNum;

        private String badCompetent;
        private Integer badCompetentNum;

        private String badArticulate;
        private Integer badArticulateNum;

        private String badThorough;
        private Integer badThoroughNum;

        private String badEngaging;
        private Integer badEngagingNum;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String teamName;
        private String teamProfile;
        private String projectName;
        private String nickname;
        private String profileImageUrl;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class myReportResponse {
        YesFeedbackInfo yesFeedbackInfo;
        NoFeedbackInfo noFeedbackInfo;
        UserInfo userInfo;
        Integer totalEvaluationNum;
        LocalDateTime lastModifiedDate;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamInfo {
        private String teamName;
        private String teamProfile;
        private String projectName;
        private Integer evaluationProgress;
        private List<String> teamUserNames;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamFeedbackInfo {
        private Integer yesFeedbackNum;
        private Integer rank;
        private Set<String> goodFeedbackContent;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamReportResponse {
        private TeamInfo teamInfo;
        private List<TeamFeedbackInfo> teamFeedbackInfoList;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SentFeedback {
        private String teamUserNickname;
        private String teamUserProfileImageUrl;
        private String teamName;
        private List<String> yesFeedbackList;
        private List<String> noFeedbackList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SentFeedbackResponse {
        private List<SentFeedback> sentFeedbackList;
    }
}
