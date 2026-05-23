package com.dealguard.message.dto;

import com.dealguard.message.ChatMessage;
import com.dealguard.message.SenderRole;
import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        Long conversationId,
        SenderRole senderRole,
        String content,
        LocalDateTime sentAt,
        int sequence,
        LocalDateTime createdAt
) {
    public static MessageResponse from(ChatMessage message) {
        return new MessageResponse(
                message.getId(),
                message.getConversation().getId(),
                message.getSenderRole(),
                message.getContent(),
                message.getSentAt(),
                message.getSequence(),
                message.getCreatedAt());
    }
}
