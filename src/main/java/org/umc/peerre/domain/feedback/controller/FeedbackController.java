package org.umc.peerre.domain.feedback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.umc.peerre.domain.feedback.dto.request.FeedbackRequest;
import org.umc.peerre.domain.feedback.dto.response.FeedbackResponse;
import org.umc.peerre.domain.feedback.service.FeedbackService;
import org.umc.peerre.global.common.SuccessResponse;
import org.umc.peerre.global.config.auth.principal.PrincipalDetails;

@RequiredArgsConstructor
@RequestMapping("/api/projects")
@RestController
public class FeedbackController {

    private final FeedbackService feedbackService;

    /**
     *
     * 테스트 시에 사용
    @Operation(summary = "피드백 등록(수정)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 피드백 등록(수정) 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
    })
    @PostMapping("/{projectId}/users/{userId}/{teamUserId}/feedback")
    public ResponseEntity<SuccessResponse<?>> enrollFeedback(@PathVariable("userId") Long userId,@PathVariable("teamUserId") Long teamUserId, @PathVariable("projectId")Long projectId
            , @RequestBody FeedbackRequest.Feedback request) {

        String response = feedbackService.enrollFeedback(userId, teamUserId,projectId,request);

        return SuccessResponse.ok(response);
    }
    */


    @Operation(summary = "피드백 등록(수정)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 피드백 등록(수정) 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
    })
    @PostMapping("/{projectId}/users/{teamUserId}/feedback")
    public ResponseEntity<SuccessResponse<?>> enrollFeedback(@PathVariable("teamUserId") Long teamUserId, @PathVariable("projectId")Long projectId
            , @RequestBody FeedbackRequest.Feedback request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long userId = principalDetails.getUser().getId();

        String response = feedbackService.enrollFeedback(userId, teamUserId,projectId,request);

        return SuccessResponse.ok(response);
    }

    /**
     *
     * 테스트 시에 사용
    @Operation(summary = "개인 리포트 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 개인 리포트 조회"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
    })
    @GetMapping("/projects/{projectId}/users/{userId}/myReport")
    public ResponseEntity<SuccessResponse<?>> getMyReport(@PathVariable("userId")Long userId,@PathVariable("projectId")Long projectId) {

        FeedbackResponse.myReportResponse myReportResponse = feedbackService.getMyReport(userId,projectId);

        return SuccessResponse.ok(myReportResponse);
    }
    */


    @Operation(summary = "개인 리포트 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 개인 리포트 조회"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
    })
    @GetMapping("/{projectId}/my-report")
    public ResponseEntity<SuccessResponse<?>> getMyReport(@PathVariable("projectId")Long projectId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long userId = principalDetails.getUser().getId();

        FeedbackResponse.myReportResponse myReportResponse = feedbackService.getMyReport(userId,projectId);

        return SuccessResponse.ok(myReportResponse);
    }

    /**
     *
     * 테스트 시에 사용
    @Operation(summary = "팀 리포트 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 팀 리포트 조회"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
    })
    @GetMapping("/projects/{projectId}/users/{userId}/teamReport")
    public ResponseEntity<SuccessResponse<?>> getTeamReport(@PathVariable("userId")Long userId,@PathVariable("projectId")Long projectId) {

        FeedbackResponse.TeamReportResponse teamReportResponse = feedbackService.getTeamReport(userId,projectId);

        return SuccessResponse.ok(teamReportResponse);
    }
    */
    @Operation(summary = "팀 리포트 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 팀 리포트 조회"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
    })
    @GetMapping("/{projectId}/team-report")
    public ResponseEntity<SuccessResponse<?>> getTeamReport(@PathVariable("projectId")Long projectId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long userId = principalDetails.getUser().getId();

        FeedbackResponse.TeamReportResponse teamReportResponse = feedbackService.getTeamReport(userId,projectId);

        return SuccessResponse.ok(teamReportResponse);
    }

    /**
     *
     * 테스트 시에 사용
    @Operation(summary = "내가 보낸 피드백 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 팀 리포트 조회"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
    })
    @GetMapping("/projects/{projectId}/users/{userId}/feedback")
    public ResponseEntity<SuccessResponse<?>> getSentFeedbacks(@PathVariable("userId")Long userId,@PathVariable("projectId")Long projectId) {

        FeedbackResponse.SentFeedbackResponse sentFeedbackResponse = feedbackService.getSentFeedbacks(userId,projectId);

        return SuccessResponse.ok(sentFeedbackResponse);
    }
    */

    @Operation(summary = "내가 보낸 피드백 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "OK, 팀 리포트 조회"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "Error Code",description = "Error message",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
    })
    @GetMapping("/{projectId}/feedback")
    public ResponseEntity<SuccessResponse<?>> getSentFeedbacks(@PathVariable("projectId")Long projectId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long userId = principalDetails.getUser().getId();

        FeedbackResponse.SentFeedbackResponse sentFeedbackResponse = feedbackService.getSentFeedbacks(userId,projectId);

        return SuccessResponse.ok(sentFeedbackResponse);
    }
}
