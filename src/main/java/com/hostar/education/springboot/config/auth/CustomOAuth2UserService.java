package com.hostar.education.springboot.config.auth;

import com.hostar.education.springboot.config.auth.dto.OAuthAttributes;
import com.hostar.education.springboot.config.auth.dto.SessionUser;
import com.hostar.education.springboot.domain.user.User;
import com.hostar.education.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /**
         * 현재 로그인 진행 중인 서비스를 구분하는 코드
         * 지금은 구글만 사용하는 불필요한 값이지만, 이후 네이버 로그인 연동 시에 네이버 로그인인지, 구글 로그인인지 구분하기 위해 사용
         */
        String registraionId = userRequest.getClientRegistration().getRegistrationId();

        /**
         * OAuth2 로그인 진행 시 키가 되는 필드값. Primary key와 같은 의미
         * 구글의 경우 기본적으로 코드를 지원하지만, 네이버 카카오 등은 기본 지원하지 않는다. 구글의 기본 코드는 "sub"이다.
         * 이후 네이버 로그인과 구글 로그인을 동시 지원할 때 사용됨
         */
        String userNameAttributeName= userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        /**
         * OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
         * 이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용
         */
        OAuthAttributes attributes = OAuthAttributes.of(registraionId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        /**
         * SessionUser
         * 세션에 사용자 정보를 저장하기 위한 Dto 클래스
         * SeesionUser에는 인증된 사용자 정보만 필요하다.그 외 필요한 정보들은 필요없으니 name, email, picture만 필드로 선언한다.
         *
         * 세션에 직접 User객체를 구현하지 않는 이유는 User클래스에 직렬화를 구현하지 않으면 에러가 난다.
         * 그렇다면 User클래스에 직렬화를 구현하면 해결될 것인가?
         * User클래스는 엔티티이기 때문에 @OneToMany, @ManyToMany 등의 자식 엔티티를 언제 갖게될지 모르므로 성능 이슈, 부수 효과가 발생활 확률이 높다.
         * 따라서 직렬화 기능을 가진 세션 Dto를 생성하면 이후 운영 및 유지보때 많은 도움이 된다.
         *
         */
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(Collections.singleton(  // singleton => 단일 원소, 원소가 딱 1개일 경우에 사용한다.
                new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        return userRepository.save(user);
    }
}
