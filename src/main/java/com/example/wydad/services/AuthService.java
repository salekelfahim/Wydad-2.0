package com.example.wydad.services;

import com.example.wydad.entities.User;
import com.example.wydad.web.VMs.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    User register(RegisterRequest registerRequest);
//    User register(User user);
    String login(String email, String password);
}