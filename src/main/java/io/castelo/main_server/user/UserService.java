package io.castelo.main_server.user;

import io.castelo.main_server.auth.jwt.AuthTokenResponse;
import io.castelo.main_server.auth.AuthenticationService;
import io.castelo.main_server.exception.EmailAlreadyRegisteredException;
import io.castelo.main_server.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
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

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void validateUser(String username) {
        userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + username));
    }

    public AuthTokenResponse login(User user) {
        return authenticationService.login(user);
    }

    public void logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
    }

    public User createUser(String email, String displayName) {
        User newUser = new User();
        newUser.setUserId(UUID.randomUUID());
        newUser.setUsername(email);
        newUser.setPassword(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()));
        newUser.setDisplayName(displayName);
        newUser.setRole(UserRoles.USER);
        newUser.setCredentialsNonExpired(true);
        newUser.setUserEnabled(true);
        return userRepository.save(newUser);
    }
}
