package com.dealguard.auth;

import com.dealguard.auth.dto.AuthResponse;
import com.dealguard.auth.dto.LoginRequest;
import com.dealguard.auth.dto.RefreshTokenRequest;
import com.dealguard.auth.dto.SignupRequest;
import com.dealguard.global.ApiResponse;
import com.dealguard.global.SecurityUtil;
import com.dealguard.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "인증", description = "회원가입, 로그인, 토큰 재발급, 현재 로그인 사용자 조회 API")
public class AuthController {

    private final AuthService authService;
    private final SecurityUtil securityUtil;

    public AuthController(AuthService authService, SecurityUtil securityUtil) {
        this.authService = authService;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 닉네임으로 새 사용자를 생성하고 access token과 refresh token을 발급합니다.")
    public ApiResponse<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ApiResponse.ok(authService.signup(request));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호를 검증한 뒤 access token, refresh token, 사용자 정보를 반환합니다.")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "refresh token을 검증한 뒤 새로운 access token과 refresh token을 재발급합니다.")
    public ApiResponse<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResponse.ok(authService.refresh(request));
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "Swagger Authorize 또는 Authorization 헤더에 입력한 access token으로 현재 로그인한 사용자 정보를 조회합니다.")
    public ApiResponse<UserResponse> me() {
        return ApiResponse.ok(UserResponse.from(securityUtil.currentUser()));
    }
}
