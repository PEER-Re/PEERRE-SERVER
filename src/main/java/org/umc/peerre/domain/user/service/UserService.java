package org.umc.peerre.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.umc.peerre.domain.user.dto.response.UserInfoResponseDto;
import org.umc.peerre.domain.user.entity.User;
import org.umc.peerre.domain.user.repository.UserRepository;
import org.umc.peerre.global.config.auth.jwt.JwtProvider;
import org.umc.peerre.global.config.auth.principal.userInfo.OAuth2UserInfo;
import org.umc.peerre.global.error.ErrorCode;
import org.umc.peerre.global.error.exception.EntityNotFoundException;
import org.umc.peerre.global.error.exception.InternalServerException;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    /**
     * 유저정보를 받아 User 객체를 반환한다.
     * 만약 회원가입되어 있지 않다면 회원가입 한다.
     * 이미 가입되어 있다면 닉네임과 프로필이미지를 업데이트한다.
     * @param userInfo 로그인을 요청하는 유저의 정보
     * @return
     */
    public User getOrCreateUser(OAuth2UserInfo userInfo) {
        String email = userInfo.getEmail();
        String nickname = userInfo.getNickname();
        String profileImgUrl = userInfo.getProfileImgUrl();

        return userRepository.findBySocialId(email)
                .map(user -> {
                    user.updateUserInfo(nickname, profileImgUrl);
                    return user;
                })
                .orElseGet(() -> createUser(email,nickname,profileImgUrl));
    }

    private User createUser(String email, String nickname, String profileImgUrl) {
        User createdUser = User.builder()
                .socialId(email)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .build();

        return userRepository.save(createdUser);
    }

    @Transactional(readOnly = true)
    public UserInfoResponseDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        return UserInfoResponseDto.of(user);
    }
}
