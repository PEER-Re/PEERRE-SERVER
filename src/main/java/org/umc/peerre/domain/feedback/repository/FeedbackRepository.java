package org.umc.peerre.domain.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.umc.peerre.domain.feedback.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
