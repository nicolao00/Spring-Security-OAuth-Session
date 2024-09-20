package com.example.springoauth2session.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomeOAuth2User implements OAuth2User {
    // OAuth2User 인터페이스를 구현한 커스텀 클래스.
    // OAuth2 인증 후 사용자의 속성(attributes)과 권한(authorities)을 관리하며, 사용자 정보를 제공하는 역할을 한다.

    private final OAuth2Response oAuth2Response;
    // OAuth2Response 객체로, OAuth2 공급자로부터 가져온 사용자 정보가 담긴다.
    private final String role;

    public CustomeOAuth2User(OAuth2Response oAuth2Response, String role) {
        this.oAuth2Response = oAuth2Response;
        this.role = role;
    }

    // Attributes: 로그인하면 리소스 서버로부터 넘어오는 모든 데이터
    // 역할: OAuth2 공급자(구글, 네이버 등)로부터 받은 사용자 정보를 속성 정보로 반환하는 메서드.
    // 반환 타입: Map<String, Object> (키와 값으로 이루어진 데이터 구조, 예: 이름, 이메일, ID 등).
    // 사용 목적: OAuth2 로그인 후, 사용자의 이름, 이메일, 프로필 사진 등의 개인 정보를 제공하기 위해 사용됩니다.
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    // Authorities: 사용자의 권한(Role)을 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role;
            }
        });

        return collection;
    }

    // 사용자의 별명이나 이름 값
    @Override
    public String getName() {
        return oAuth2Response.getName();
    }

    // 특정한 아이디 값을 지정해줌
    public String getUsername() {
        return oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
    }
}
