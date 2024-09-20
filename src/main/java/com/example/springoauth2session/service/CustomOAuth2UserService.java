package com.example.springoauth2session.service;

import com.example.springoauth2session.dto.*;
import com.example.springoauth2session.dto.GoogleResponse;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    // DefaultOAuth2UserService는 Spring Security가 제공하는 기본 OAuth2UserService 구현체로,
    // OAuth2 사용자 정보를 가져오는 역할을 한다.


    // OAuth2 인증 요청이 들어오면, 사용자의 정보를 가져오는 메서드를 재정의한다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 부모 클래스인 DefaultOAuth2UserService의 loadUser() 메서드를 호출하여,
        // OAuth2 공급자로부터 사용자 정보를 가져온다.
        // 이 정보는 OAuth2User 객체로 반환되며, 사용자의 속성(attribute)을 포함하고 있다.

        System.out.println(oAuth2User.getAttributes());
        // 가져온 사용자 정보의 속성(attributes)을 콘솔에 출력한다.
        // 이 속성은 JSON 형태의 사용자 정보로, 이름, 이메일 등을 포함한다.

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // userRequest에서 현재 인증 요청을 보낸 OAuth2 공급자의 식별자(registrationId)를 가져온다.
        // 이 값은 "naver", "google" 등으로, 어떤 OAuth2 공급자를 사용하는지 확인할 수 있다.

        OAuth2Response oAuth2Response = null;
        // OAuth2Response 타입의 객체를 선언.
        // 이 객체는 각 OAuth2 공급자로부터 받은 사용자 정보를 파싱하여 사용하기 위한 클래스가 될 것이다.

        if (registrationId.equals("naver")) {
            // 현재 OAuth2 공급자가 "naver"인 경우
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
            // NaverResponse 객체를 생성하고, 네이버에서 받은 사용자 속성 정보를 인자로 넘겨준다.
            // NaverResponse는 네이버 사용자 정보에 맞춰 파싱하는 클래스여야 한다.
        }
        else if (registrationId.equals("google")) {
            // 현재 OAuth2 공급자가 "google"인 경우
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
            // GoogleResponse 객체를 생성하고, 구글에서 받은 사용자 속성 정보를 인자로 넘겨준다.
            // GoogleResponse는 구글 사용자 정보에 맞춰 파싱하는 클래스여야 함.
        }
        else {
            // 네이버나 구글 이외의 다른 OAuth2 공급자는 지원하지 않음
            return null;
            // 해당 OAuth2 공급자를 처리할 수 없으므로, null을 반환한다.
        }

        String role = "ROLE_USER";

        return new CustomeOAuth2User(oAuth2Response, role);

    }
}
