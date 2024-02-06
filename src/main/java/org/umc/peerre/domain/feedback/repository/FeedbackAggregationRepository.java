package org.umc.peerre.domain.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.umc.peerre.domain.feedback.entity.FeedbackAggregation;

@Repository
public interface FeedbackAggregationRepository extends JpaRepository<FeedbackAggregation,Long> {
}
