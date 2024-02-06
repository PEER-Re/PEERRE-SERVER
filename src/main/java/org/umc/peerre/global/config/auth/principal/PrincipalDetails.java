package org.umc.peerre.global.config.auth.principal;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.umc.peerre.domain.user.entity.User;

import java.util.Collection;
import java.util.Map;

@Data //굳이 이것까지...? getter면 되지않나요
public class PrincipalDetails implements OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public PrincipalDetails(User user,Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //유저의 역할은 팀에 소속되는 순간부터 생긴다. e.g. 멤버, 팀장
        //회원가입 시 유저의 역할은 없고 인증만 해주면 되므로 null을 반환한다.
        return null;
    }

    @Override
    public String getName() {
        return user.getNickname();
    }

    public String getSocialId() {
        return user.getSocialId();
    }
}
