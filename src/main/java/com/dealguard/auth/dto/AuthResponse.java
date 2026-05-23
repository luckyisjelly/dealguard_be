package com.dealguard.auth.dto;

import com.dealguard.user.UserResponse;

public record AuthResponse(String accessToken, String refreshToken, UserResponse user) {
}
