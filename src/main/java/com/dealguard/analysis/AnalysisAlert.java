package com.dealguard.analysis;

import com.dealguard.conversation.Conversation;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AnalysisAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false)
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertSeverity severity;

    @Column(name = "field_name")
    private String fieldName;

    @Column(nullable = false, columnDefinition = "text")
    private String message;

    @Column(name = "before_value", columnDefinition = "text")
    private String beforeValue;

    @Column(name = "after_value", columnDefinition = "text")
    private String afterValue;

    @Column(name = "source_message_ids")
    private String sourceMessageIds;

    @Column(nullable = false)
    private boolean resolved;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected AnalysisAlert() {
    }

    public AnalysisAlert(Conversation conversation, AlertType alertType, AlertSeverity severity, String fieldName,
            String message, String beforeValue, String afterValue, String sourceMessageIds) {
        this.conversation = conversation;
        this.alertType = alertType;
        this.severity = severity;
        this.fieldName = fieldName;
        this.message = message;
        this.beforeValue = beforeValue;
        this.afterValue = afterValue;
        this.sourceMessageIds = sourceMessageIds;
    }

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Conversation getConversation() { return conversation; }
    public AlertType getAlertType() { return alertType; }
    public AlertSeverity getSeverity() { return severity; }
    public String getFieldName() { return fieldName; }
    public String getMessage() { return message; }
    public String getBeforeValue() { return beforeValue; }
    public String getAfterValue() { return afterValue; }
    public String getSourceMessageIds() { return sourceMessageIds; }
    public boolean isResolved() { return resolved; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
