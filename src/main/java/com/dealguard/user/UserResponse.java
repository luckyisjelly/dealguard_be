package com.dealguard.user;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String nickname,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
