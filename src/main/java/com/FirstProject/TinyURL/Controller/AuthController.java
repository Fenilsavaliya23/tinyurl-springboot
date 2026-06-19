package com.FirstProject.TinyURL.Controller;

import com.FirstProject.TinyURL.dto.AuthResponse;
import com.FirstProject.TinyURL.dto.LoginRequest;
import com.FirstProject.TinyURL.dto.LoginResponse;
import com.FirstProject.TinyURL.dto.SignupRequest;
import com.FirstProject.TinyURL.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
            return ResponseEntity.ok(new AuthResponse(authService.signup(signupRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok((authService.login(loginRequest)));
    }

}
