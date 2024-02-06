package org.umc.peerre.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.umc.peerre.domain.feedback.entity.Comment;
import org.umc.peerre.domain.feedback.entity.FeedbackAggregation;
import org.umc.peerre.domain.feedback.entity.FeedbackRegistration;
import org.umc.peerre.domain.project.constant.Status;
import org.umc.peerre.domain.teamspace.entity.Teamspace;
import org.umc.peerre.global.common.BaseTimeEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name="Project")
@Table(name="project")
public class Project extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String title;

    @Column
    private Integer size;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    @Column(name="start_day")
    private LocalDate startDay;

    @Column(name="end_day")
    private LocalDate endDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id", referencedColumnName = "id")
    private Teamspace teamspace;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<FeedbackRegistration> feedbackRegistrationList = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<FeedbackAggregation> feedbackAggregationList = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();



}
