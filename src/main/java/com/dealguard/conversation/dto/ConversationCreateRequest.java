package com.dealguard.conversation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConversationCreateRequest(
        @NotNull Long productPostId,
        @NotBlank String title
) {
}
