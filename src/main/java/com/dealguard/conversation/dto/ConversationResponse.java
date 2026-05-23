package com.dealguard.conversation.dto;

import com.dealguard.conversation.Conversation;
import com.dealguard.conversation.ConversationStatus;
import java.time.LocalDateTime;

public record ConversationResponse(
        Long id,
        Long productPostId,
        Long ownerUserId,
        String title,
        ConversationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ConversationResponse from(Conversation conversation) {
        return new ConversationResponse(
                conversation.getId(),
                conversation.getProductPost().getId(),
                conversation.getOwnerUser().getId(),
                conversation.getTitle(),
                conversation.getStatus(),
                conversation.getCreatedAt(),
                conversation.getUpdatedAt());
    }
}
