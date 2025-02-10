package io.castelo.main_server.auth;

import io.castelo.main_server.auth.jwt.AuthTokenResponse;
import io.castelo.main_server.auth.jwt.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JWTService jwtService;


    @Autowired
    public AuthenticationService(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    public AuthTokenResponse login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            return generateToken(authentication);
        } else {
            return null;
        }
    }

    private AuthTokenResponse generateToken(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }

    public void logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
    }
}
