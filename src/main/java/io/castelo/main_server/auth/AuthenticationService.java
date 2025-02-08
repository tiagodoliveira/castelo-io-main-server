package io.castelo.main_server.auth;

import io.castelo.main_server.auth.jwt.AuthTokenResponse;
import io.castelo.main_server.auth.jwt.JWTService;
import io.castelo.main_server.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthTokenResponse login(User user) {
        Authentication authentication = authenticate(user);
        if (authentication.isAuthenticated()) {
            return generateToken(authentication);
        } else {
            return null;
        }
    }

    private Authentication authenticate(User user) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    }

    private AuthTokenResponse generateToken(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }

}
