package com.ecommerce.service;

import com.ecommerce.dto.AuthRequest;
import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.RegisterRequest;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Authentication Service Implementation
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsernameOrEmail(),
                        authRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication.getName());

        // Update last login
        User user = userRepository.findByUsername(authRequest.getUsernameOrEmail())
                .orElseGet(() -> userRepository.findByEmail(authRequest.getUsernameOrEmail())
                        .orElse(null));

        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            return AuthResponse.builder()
                    .accessToken(jwt)
                    .tokenType("Bearer")
                    .userId(user.getUserId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getUserRole().name())
                    .build();
        }

        throw new BadRequestException("Authentication failed");
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        // Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        // Create new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhone(registerRequest.getPhone());
        user.setIsActive(true);
        user.setIsVerified(false);
        user.setUserRole(User.UserRole.CUSTOMER);

        User savedUser = userRepository.save(user);

        // Generate JWT token
        String jwt = tokenProvider.generateToken(savedUser.getUsername());

        logger.info("User registered successfully: {}", savedUser.getUsername());

        return AuthResponse.builder()
                .accessToken(jwt)
                .tokenType("Bearer")
                .userId(savedUser.getUserId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getUserRole().name())
                .build();
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
