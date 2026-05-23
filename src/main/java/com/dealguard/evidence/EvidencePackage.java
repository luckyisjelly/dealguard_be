package com.dealguard.evidence;

import com.dealguard.conversation.Conversation;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class EvidencePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "sha256_hash", nullable = false)
    private String sha256Hash;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    protected EvidencePackage() {
    }

    public EvidencePackage(Conversation conversation, String fileName, String filePath, String sha256Hash) {
        this.conversation = conversation;
        this.fileName = fileName;
        this.filePath = filePath;
        this.sha256Hash = sha256Hash;
        this.generatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Conversation getConversation() { return conversation; }
    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public String getSha256Hash() { return sha256Hash; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
}
