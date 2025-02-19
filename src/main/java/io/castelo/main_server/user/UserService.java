package io.castelo.main_server.user;

import io.castelo.main_server.auth.PasswordEncoder;
import io.castelo.main_server.exception.EmailAlreadyRegisteredException;
import io.castelo.main_server.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public User createUser(User user) {
        userRepository.findByEmail(user.getUsername()).ifPresent(_ -> {
            throw new EmailAlreadyRegisteredException(user.getUsername());
        });
        user.setUserId(UUID.randomUUID());
        user.setPassword(PasswordEncoder.encodePassword(user.getPassword()));
        return userRepository.save(user);
    }

    public User createUser(String email, String displayName) {
        User newUser = new User();
        newUser.setUserId(UUID.randomUUID());
        newUser.setUsername(email);
        newUser.setPassword(PasswordEncoder.encodePassword(UUID.randomUUID().toString()));
        newUser.setDisplayName(displayName);
        newUser.setRole(UserRoles.USER);
        newUser.setCredentialsNonExpired(true);
        newUser.setUserEnabled(true);
        return userRepository.save(newUser);
    }

    @PreAuthorize("#userDetails.username == authentication.name or hasAuthority('ADMIN')")
    public User updateUserDisplayName(User userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userDetails.getUsername()));

        user.setDisplayName(userDetails.getDisplayName());
        return userRepository.save(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        userRepository.delete(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
