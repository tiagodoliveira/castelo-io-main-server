package io.castelo.main_server.auth;

import io.castelo.main_server.user.User;
import io.castelo.main_server.user.UserRoles;
import io.castelo.main_server.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Autowired
    public CustomOAuth2UserService(@Lazy UserService userService) {
        this.userService = userService;
    }

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email;

        if ("github".equalsIgnoreCase(userRequest.getClientRegistration().getRegistrationId())) {
            email = fetchEmailFromGitHub(userRequest.getAccessToken().getTokenValue());
        } else {
            email = oAuth2User.getAttribute("email");
        }

        if (email != null) {
            userService.verifyIfUserExists(email).orElseGet(() -> {
                User newUser = new User();
                newUser.setUserId(UUID.randomUUID());
                newUser.setEmail(email);
                newUser.setPassword(bCryptPasswordEncoder.encode(UUID.randomUUID().toString())); // Set a random password
                newUser.setDisplayName(oAuth2User.getName());
                newUser.setRole(UserRoles.USER);
                newUser.setCredentialsNonExpired(true);
                newUser.setUserEnabled(true);
                return userService.createUser(newUser);
            });
        }

        return oAuth2User;
    }

    private String fetchEmailFromGitHub(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );

        List<Map<String, Object>> emails = response.getBody();
        if (emails != null && !emails.isEmpty()) {
            for (Map<String, Object> email : emails) {
                if (Boolean.TRUE.equals(email.get("primary")) && Boolean.TRUE.equals(email.get("verified"))) {
                    return (String) email.get("email");
                }
            }
        }
        return null;
    }
}