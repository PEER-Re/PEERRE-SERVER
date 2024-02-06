package org.umc.peerre.domain.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.umc.peerre.domain.feedback.entity.FeedbackRegistration;
import org.umc.peerre.domain.project.entity.Project;
import org.umc.peerre.domain.user.entity.User;

@Repository
public interface FeedbackRegistrationRepository extends JpaRepository<FeedbackRegistration,Long> {
    boolean existsByRecipientIdAndUserAndProject(Long recipientId, User user, Project project);
    FeedbackRegistration findByRecipientIdAndUserAndProject(Long recipientId, User user, Project project);
}
