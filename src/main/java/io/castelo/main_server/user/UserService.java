package io.castelo.main_server.user;

import io.castelo.main_server.auth.AuthTokenResponse;
import io.castelo.main_server.auth.JWTService;
import io.castelo.main_server.exception.EmailAlreadyRegisteredException;
import io.castelo.main_server.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @PreAuthorize("authentication.authorities.contains('ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public User createUser(User user) {
        userRepository.findByEmail(user.getUsername()).ifPresent(_ -> {
            throw new EmailAlreadyRegisteredException(user.getUsername());
        });
        user.setUserId(UUID.randomUUID());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @PreAuthorize("#userDetails.username == authentication.name")
    public User updateUserDisplayName(User userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userDetails.getUsername()));

        user.setDisplayName(userDetails.getDisplayName());
        return userRepository.save(user);
    }

    @PreAuthorize("authentication.authorities.contains('ADMIN')")
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        userRepository.delete(user);
    }

    public void verifyIfUserExists(UUID userId) {
        if(!userRepository.existsById(userId))
            throw new ResourceNotFoundException("User not found with id: " + userId);
    }

    public AuthTokenResponse login(User user) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(authentication);
        } else {
            return null;
        }
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
