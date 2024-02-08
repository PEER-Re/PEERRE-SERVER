package org.umc.peerre.domain.teamspace.entity;

import jakarta.persistence.*;
import lombok.*;
import org.umc.peerre.domain.teamspace.constant.Role;
import org.umc.peerre.domain.user.entity.User;
import org.umc.peerre.global.common.BaseTimeEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name="UserTeamSpace")
@Table(name="user_teamspace")
public class UserTeamspace extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id", referencedColumnName = "id")
    private Teamspace teamspace;
}
