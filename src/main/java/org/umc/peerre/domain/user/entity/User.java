package org.umc.peerre.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.umc.peerre.domain.feedback.entity.Comment;
import org.umc.peerre.domain.feedback.entity.FeedbackAggregation;
import org.umc.peerre.domain.feedback.entity.FeedbackRegistration;
import org.umc.peerre.domain.teamspace.entity.UserTeamspace;
import org.umc.peerre.global.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name="User")
@Table(name="user")
public class User extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String social_id;

    @Column
    private String nickname;

    @Column
    private String profileimg_url;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FeedbackRegistration> feedbackRegistrationList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FeedbackAggregation> feedbackAggregationList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTeamspace> userTeamspaceList = new ArrayList<>();

}
