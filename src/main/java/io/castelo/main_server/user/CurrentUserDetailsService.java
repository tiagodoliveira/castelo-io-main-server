package io.castelo.main_server.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class CurrentUserDetailsService  implements UserDetailsService {

    private final UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(CurrentUserDetailsService.class);

    @Autowired
    public CurrentUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Authenticating user: {}", email);
        return userRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
