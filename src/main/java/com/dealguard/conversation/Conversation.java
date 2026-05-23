package com.dealguard.conversation;

import com.dealguard.product.ProductPost;
import com.dealguard.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_post_id")
    private ProductPost productPost;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_user_id")
    private User ownerUser;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConversationStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Conversation() {
    }

    public Conversation(ProductPost productPost, User ownerUser, String title) {
        this.productPost = productPost;
        this.ownerUser = ownerUser;
        this.title = title;
        this.status = ConversationStatus.ACTIVE;
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void close() {
        status = ConversationStatus.CLOSED;
    }

    public Long getId() { return id; }
    public ProductPost getProductPost() { return productPost; }
    public User getOwnerUser() { return ownerUser; }
    public String getTitle() { return title; }
    public ConversationStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
