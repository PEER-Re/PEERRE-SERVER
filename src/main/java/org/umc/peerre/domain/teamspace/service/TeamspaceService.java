package org.umc.peerre.domain.teamspace.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.umc.peerre.domain.project.entity.Project;
import org.umc.peerre.domain.teamspace.constant.Role;
import org.umc.peerre.domain.teamspace.dto.request.CreateTeamspaceRequestDto;
import org.umc.peerre.domain.teamspace.dto.response.*;
import org.umc.peerre.domain.teamspace.entity.Teamspace;
import org.umc.peerre.domain.teamspace.entity.UserTeamspace;
import org.umc.peerre.domain.teamspace.repository.TeamspaceRepository;
import org.umc.peerre.domain.teamspace.repository.UserTeamspaceRepository;
import org.umc.peerre.domain.user.entity.User;
import org.umc.peerre.domain.user.repository.UserRepository;
import org.umc.peerre.global.error.ErrorCode;
import org.umc.peerre.global.error.exception.ConflictException;
import org.umc.peerre.global.error.exception.ForbiddenException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class TeamspaceService {

    private final TeamspaceRepository teamspaceRepository;
    private final UserRepository userRepository;
    private final UserTeamspaceRepository userTeamspaceRepository;
    private final int randomStrLen = 4; // 초대 코드 길이

    public CreateTeamspaceResponseDto createTeamspace(Long userId, CreateTeamspaceRequestDto createTeamspaceRequestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        String name = createTeamspaceRequestDto.name();
        String profile = createTeamspaceRequestDto.profile();

        String invitationCode = CreateInvitationCode(randomStrLen);
        teamspaceRepository.findByInvitationCode(invitationCode)
                .ifPresent( a -> { throw new ConflictException(ErrorCode.CODE_CONFLICT); });

        Teamspace teamspace = Teamspace.builder()
                .name(name)
                .profile(profile)
                .invitationCode(invitationCode)
                .size(1)
                .leaderId(userId)
                .build();

        UserTeamspace userTeamspace = UserTeamspace.builder()
                .user(user)
                .role(Role.Leader)
                .teamspace(teamspace)
                .build();

        teamspaceRepository.save(teamspace);
        userTeamspaceRepository.save(userTeamspace);

        return CreateTeamspaceResponseDto.of(teamspace);
    }

    public String CreateInvitationCode(int randomStrLen){
        String randomInvitationCode = RandomStringUtils.randomAlphanumeric(randomStrLen);
        return randomInvitationCode;
    }

    public TeamspacesResponseDto getTeamspaces(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        List<UserTeamspace> userTeamspaceList = userTeamspaceRepository.findByUser(user)
                .orElseThrow(()-> new EntityNotFoundException("해당 유저가 속해있는 팀 스페이스가 없습니다."));

        List<TeamspaceResponseDto> teamspaceResponseDtoList = userTeamspaceList.stream()
                .map(userTeamspace -> TeamspaceResponseDto.of(userTeamspace))
                .collect(Collectors.toList());

        return TeamspacesResponseDto.of(teamspaceResponseDtoList);
    }

    public ProjectsResponseDto getProjects(Long teamspaceId) {

        Teamspace teamspace = teamspaceRepository.findById(teamspaceId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TEAM_NOT_FOUND.getMessage()));

        List<Project> projectList = teamspace.getProjectList();

        List<ProjectResponseDto> projectResponseDtoList = projectList.stream()
                .map(project -> ProjectResponseDto.of(project))
                .collect(Collectors.toList());

        return ProjectsResponseDto.of(projectResponseDtoList);
    }

    public boolean deleteTeamspace(Long userId, Long teamspaceId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        Teamspace teamspace = teamspaceRepository.findById(teamspaceId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TEAM_NOT_FOUND.getMessage()));

        UserTeamspace userTeamspace = userTeamspaceRepository.findByUserIdAndTeamspaceId(userId, teamspaceId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND.getMessage()));

        if (userTeamspace.getRole()!=Role.Leader){
            throw new ForbiddenException(ErrorCode.TEAM_DELETE_FORBIDDEN);
        }

        List<UserTeamspace> userTeamspaceList = userTeamspaceRepository.findByTeamspaceId(teamspaceId);
        userTeamspaceRepository.deleteAll(userTeamspaceList);

        teamspaceRepository.deleteById(teamspaceId);

        return Boolean.TRUE;
    }
    @Transactional
    public TeamspaceResponseDto inviteMember(Long userId, String invitationCode) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        Teamspace teamspace = teamspaceRepository.findByInvitationCode(invitationCode)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TEAM_NOT_FOUND.getMessage()));

        int tmpSize = teamspace.getSize();
        int resultSize = tmpSize+1;
        teamspace.setSize(resultSize);
        System.out.println(teamspace.getSize());
        UserTeamspace userTeamspace = UserTeamspace.builder()
                .user(user)
                .role(Role.Member)
                .teamspace(teamspace)
                .build();

        userTeamspaceRepository.save(userTeamspace);

        return TeamspaceResponseDto.of(userTeamspace);
    }
}
