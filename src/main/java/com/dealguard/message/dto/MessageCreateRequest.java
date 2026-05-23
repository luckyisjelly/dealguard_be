package com.dealguard.message.dto;

import com.dealguard.message.SenderRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record MessageCreateRequest(
        @NotNull SenderRole senderRole,
        @NotBlank String content,
        LocalDateTime sentAt
) {
}
