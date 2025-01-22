package io.castelo.main_server.user;

import io.castelo.main_server.exception.EmailAlreadyRegisteredException;
import io.castelo.main_server.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        return userRepository.save(user);
    }

    public User updateUserDisplayName(UUID userId, User userDetails) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setDisplayName(userDetails.getDisplayName());
        return userRepository.save(user);
    }

    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        userRepository.delete(user);
    }

    public void verifyIfUserExists(UUID userId) {
        if(!userRepository.existsById(userId))
            throw new ResourceNotFoundException("User not found with id: " + userId);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
