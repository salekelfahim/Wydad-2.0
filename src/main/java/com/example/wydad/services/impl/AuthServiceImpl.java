package com.example.wydad.services.impl;

import com.example.wydad.entities.User;
import com.example.wydad.repositories.UserRepository;
import com.example.wydad.security.JwtService;
import com.example.wydad.services.AuthService;
import com.example.wydad.web.VMs.RegisterRequest;
import com.example.wydad.web.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User register(RegisterRequest registerRequest) {
        try {
            User user = User.builder()
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .role(registerRequest.getRole())
                    .build();

            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error registering user", e);
        }
    }

//    @Override
//    public User register(User user) {
//        try {
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//            return userRepository.save(user);
//        } catch (Exception e) {
//            throw new RuntimeException("Error registering user", e);
//        }
//    }

    @Override
    public String login(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found for this email :: " + email));
            return jwtService.generateToken(userDetails, user.getRole().name(), user.getId());
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password", e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}