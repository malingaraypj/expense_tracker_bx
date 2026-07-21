package org.example.expense_tracker_bx.service;


import org.example.expense_tracker_bx.dto.request.LoginRequest;
import org.example.expense_tracker_bx.dto.request.RegisterRequest;
import org.example.expense_tracker_bx.dto.response.AuthResponse;
import org.example.expense_tracker_bx.entity.User;
import org.example.expense_tracker_bx.entity.enums.Role;
import org.example.expense_tracker_bx.repository.UserRepository;
import org.example.expense_tracker_bx.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Hash password!
                .role(Role.ROLE_USER)
                .isEnabled(true)
                .build();

        User savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .token(jwtToken)
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // Authenticate credentials against database; throws exception if invalid
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
