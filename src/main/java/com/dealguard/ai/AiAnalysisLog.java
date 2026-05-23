package com.dealguard.ai;

import com.dealguard.conversation.Conversation;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AiAnalysisLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    private String provider;

    @Column(name = "request_summary", columnDefinition = "text")
    private String requestSummary;

    @Column(name = "response_summary", columnDefinition = "text")
    private String responseSummary;

    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected AiAnalysisLog() {
    }

    public AiAnalysisLog(Conversation conversation, String provider, String requestSummary, String responseSummary, String status) {
        this.conversation = conversation;
        this.provider = provider;
        this.requestSummary = requestSummary;
        this.responseSummary = responseSummary;
        this.status = status;
    }

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
