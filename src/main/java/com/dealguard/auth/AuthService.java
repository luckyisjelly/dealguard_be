package com.dealguard.auth;

import com.dealguard.auth.dto.AuthResponse;
import com.dealguard.auth.dto.LoginRequest;
import com.dealguard.auth.dto.SignupRequest;
import com.dealguard.global.BadRequestException;
import com.dealguard.user.User;
import com.dealguard.user.UserRepository;
import com.dealguard.user.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("email already exists");
        }
        User user = userRepository.save(new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.nickname()));
        return new AuthResponse(jwtTokenProvider.createToken(user.getEmail()), UserResponse.from(user));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("invalid email or password"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadRequestException("invalid email or password");
        }
        return new AuthResponse(jwtTokenProvider.createToken(user.getEmail()), UserResponse.from(user));
    }
}
