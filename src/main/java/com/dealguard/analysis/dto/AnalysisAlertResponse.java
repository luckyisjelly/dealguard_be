package com.dealguard.analysis.dto;

import com.dealguard.analysis.AlertSeverity;
import com.dealguard.analysis.AlertType;
import com.dealguard.analysis.AnalysisAlert;
import java.time.LocalDateTime;

public record AnalysisAlertResponse(
        Long id,
        Long conversationId,
        AlertType alertType,
        AlertSeverity severity,
        String fieldName,
        String message,
        String beforeValue,
        String afterValue,
        String sourceMessageIds,
        boolean resolved,
        LocalDateTime createdAt
) {
    public static AnalysisAlertResponse from(AnalysisAlert alert) {
        return new AnalysisAlertResponse(
                alert.getId(),
                alert.getConversation().getId(),
                alert.getAlertType(),
                alert.getSeverity(),
                alert.getFieldName(),
                alert.getMessage(),
                alert.getBeforeValue(),
                alert.getAfterValue(),
                alert.getSourceMessageIds(),
                alert.isResolved(),
                alert.getCreatedAt());
    }
}
