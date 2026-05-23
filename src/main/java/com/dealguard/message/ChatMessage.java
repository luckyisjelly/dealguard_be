package com.dealguard.message;

import com.dealguard.conversation.Conversation;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_role", nullable = false)
    private SenderRole senderRole;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @Column(name = "message_sequence", nullable = false)
    private int sequence;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected ChatMessage() {
    }

    public ChatMessage(Conversation conversation, SenderRole senderRole, String content, LocalDateTime sentAt, int sequence) {
        this.conversation = conversation;
        this.senderRole = senderRole;
        this.content = content;
        this.sentAt = sentAt == null ? LocalDateTime.now() : sentAt;
        this.sequence = sequence;
    }

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Conversation getConversation() { return conversation; }
    public SenderRole getSenderRole() { return senderRole; }
    public String getContent() { return content; }
    public LocalDateTime getSentAt() { return sentAt; }
    public int getSequence() { return sequence; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
