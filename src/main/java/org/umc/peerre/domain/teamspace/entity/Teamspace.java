package org.umc.peerre.domain.teamspace.entity;

import jakarta.persistence.*;
import lombok.*;
import org.umc.peerre.domain.project.entity.Project;
import org.umc.peerre.global.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name="Teamspace")
@Table(name="teamspace")
public class Teamspace extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private Integer size;

    @Column
    private String name;

    @Column
    private String profile;

    @Column
    private String invitation_code;

    @OneToMany(mappedBy = "teamspace", cascade = CascadeType.ALL)
    private List<UserTeamspace> userTeamspaceList = new ArrayList<>();

    @OneToMany(mappedBy = "teamSpace", cascade = CascadeType.ALL)
    private List<Project> projectList = new ArrayList<>();
}
