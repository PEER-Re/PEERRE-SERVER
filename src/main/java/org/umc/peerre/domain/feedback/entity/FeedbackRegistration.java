package org.umc.peerre.domain.feedback.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.umc.peerre.domain.project.entity.Project;
import org.umc.peerre.domain.user.entity.User;
import org.umc.peerre.global.common.BaseTimeEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name="FeedbackRegistration")
@Table(name="feedback_registration")
public class FeedbackRegistration extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name="recipient_id")
    private Long recipientId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id", referencedColumnName = "id")
    private Project project;

    @OneToMany(mappedBy = "feedbackRegistration", cascade = CascadeType.ALL)
    private List<Feedback> feedbackList = new ArrayList<>();

}
