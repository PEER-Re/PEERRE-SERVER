package org.umc.peerre.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.umc.peerre.domain.project.entity.Project;
import org.umc.peerre.domain.user.entity.User;
import org.umc.peerre.global.common.BaseTimeEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name="Comment")
@Table(name="comment")
public class Comment extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id", referencedColumnName = "id")
    private Project project;
}