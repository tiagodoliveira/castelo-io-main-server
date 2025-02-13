package io.castelo.main_server.auth;

import io.castelo.main_server.client.github.GitHubService;
import io.castelo.main_server.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;
    private final GitHubService gitHubService;

    @Autowired
    public CustomOAuth2UserService(UserService userService, GitHubService gitHubService) {
        this.userService = userService;
        this.gitHubService = gitHubService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email;

        if ("github".equalsIgnoreCase(userRequest.getClientRegistration().getRegistrationId())) {
            email = gitHubService.fetchEmailFromGitHub("token " + userRequest.getAccessToken().getTokenValue());
        } else {
            email = oAuth2User.getAttribute("email");
        }

        if (email != null) {
            ensureUserExists(email, oAuth2User.getName());
        }

        return oAuth2User;
    }

    private void ensureUserExists(String email, String displayName) {
        userService.getUserByEmail(email).orElseGet(() -> userService.createUser(email, displayName));
    }

}