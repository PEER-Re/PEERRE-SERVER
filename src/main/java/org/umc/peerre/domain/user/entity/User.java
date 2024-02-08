package org.umc.peerre.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.umc.peerre.domain.project.entity.Comment;
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

    @Column(name="social_id")
    private String socialId;

    @Column
    private String nickname;

    @Column
    private String profileImgUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FeedbackRegistration> feedbackRegistrationList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FeedbackAggregation> feedbackAggregationList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTeamspace> userTeamspaceList = new ArrayList<>();

    public void updateUserInfo(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImgUrl = profileImageUrl;
    }
}
