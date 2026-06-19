package com.FirstProject.TinyURL.service;

import com.FirstProject.TinyURL.Model.User;
import com.FirstProject.TinyURL.dto.LoginRequest;
import com.FirstProject.TinyURL.dto.LoginResponse;
import com.FirstProject.TinyURL.dto.SignupRequest;
import com.FirstProject.TinyURL.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.FirstProject.TinyURL.exception.UserNotFoundException;
import com.FirstProject.TinyURL.exception.InvalidPasswordException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String signup(SignupRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .build();

        userRepository.save(user);

        return "User Registered Successfully";
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User not Found"));

        if(!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidPasswordException("Password Mismatch, please Enter Correct Password");
        }

        return new LoginResponse(
                "Login Successfully",
                jwtService.generateToken(user.getEmail())
        );
    }
}
