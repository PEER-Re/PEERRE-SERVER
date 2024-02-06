package org.umc.peerre.domain.feedback.entity;

import jakarta.persistence.*;
import lombok.*;
import org.umc.peerre.global.common.BaseTimeEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name="Feedback")
@Table(name="feedback")
public class Feedback  extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String feedback_content;

    @Column
    private Boolean feedback_type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="feedbackregistration_id", referencedColumnName = "id")
    private FeedbackRegistration feedbackRegistration;
}
