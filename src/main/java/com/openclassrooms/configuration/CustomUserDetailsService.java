package com.openclassrooms.configuration;

import java.util.Optional;

import com.openclassrooms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService implementation for Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Loads a user by their email address for Spring Security authentication.
     *
     * @param email The email address of the user.
     * @return UserDetails representing the authenticated user.
     * @throws UsernameNotFoundException If the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Optional<com.openclassrooms.entity.User> optionalUser = userRepository.findByEmail(email);

        com.openclassrooms.entity.User user = optionalUser.orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + email)
        );

        return new User(user.getEmail(), user.getPassword(), null);
    }
}