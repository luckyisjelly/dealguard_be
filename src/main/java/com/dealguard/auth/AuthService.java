package com.dealguard.auth;

import com.dealguard.auth.dto.AuthResponse;
import com.dealguard.auth.dto.LoginRequest;
import com.dealguard.auth.dto.RefreshTokenRequest;
import com.dealguard.auth.dto.SignupRequest;
import com.dealguard.global.BadRequestException;
import com.dealguard.global.UnauthorizedException;
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
        return createAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("invalid email or password"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadRequestException("invalid email or password");
        }
        return createAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse refresh(RefreshTokenRequest request) {
        String email;
        try {
            email = jwtTokenProvider.getRefreshTokenSubject(request.refreshToken());
        } catch (RuntimeException ex) {
            throw new UnauthorizedException("유효하지 않거나 만료된 refresh token입니다. 다시 로그인해 주세요.");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("유효하지 않은 refresh token입니다. 다시 로그인해 주세요."));
        return createAuthResponse(user);
    }

    private AuthResponse createAuthResponse(User user) {
        return new AuthResponse(
                jwtTokenProvider.createAccessToken(user.getEmail()),
                jwtTokenProvider.createRefreshToken(user.getEmail()),
                UserResponse.from(user));
    }
}
