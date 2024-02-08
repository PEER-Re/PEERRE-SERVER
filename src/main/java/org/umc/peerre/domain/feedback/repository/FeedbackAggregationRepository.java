package org.umc.peerre.domain.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.umc.peerre.domain.feedback.entity.FeedbackAggregation;
import org.umc.peerre.domain.project.entity.Project;
import org.umc.peerre.domain.user.entity.User;

import java.util.Optional;

@Repository
public interface FeedbackAggregationRepository extends JpaRepository<FeedbackAggregation,Long> {
    Integer countByProjectIdAndUserIdAndEvaluationStatus(Long userId, Long projectId, Boolean evaluationStatus);
    Optional<FeedbackAggregation> findByUserAndProject(User user, Project project);
}
