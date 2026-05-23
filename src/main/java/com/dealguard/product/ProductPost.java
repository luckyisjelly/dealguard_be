package com.dealguard.product;

import com.dealguard.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ProductPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_user_id")
    private User ownerUser;

    @Column(nullable = false)
    private String title;

    private String category;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "listed_price")
    private Integer listedPrice;

    @Column(name = "condition_description", columnDefinition = "text")
    private String conditionDescription;

    @Column(name = "defect_description", columnDefinition = "text")
    private String defectDescription;

    @Column(name = "refund_policy_text", columnDefinition = "text")
    private String refundPolicyText;

    @Column(name = "trade_location_text")
    private String tradeLocationText;

    @Column(name = "delivery_available", nullable = false)
    private boolean deliveryAvailable;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected ProductPost() {
    }

    public ProductPost(User ownerUser, String title, String category, String description, Integer listedPrice,
            String conditionDescription, String defectDescription, String refundPolicyText,
            String tradeLocationText, boolean deliveryAvailable) {
        this.ownerUser = ownerUser;
        update(title, category, description, listedPrice, conditionDescription, defectDescription,
                refundPolicyText, tradeLocationText, deliveryAvailable);
    }

    public void update(String title, String category, String description, Integer listedPrice,
            String conditionDescription, String defectDescription, String refundPolicyText,
            String tradeLocationText, Boolean deliveryAvailable) {
        if (title != null) this.title = title;
        if (category != null) this.category = category;
        if (description != null) this.description = description;
        if (listedPrice != null) this.listedPrice = listedPrice;
        if (conditionDescription != null) this.conditionDescription = conditionDescription;
        if (defectDescription != null) this.defectDescription = defectDescription;
        if (refundPolicyText != null) this.refundPolicyText = refundPolicyText;
        if (tradeLocationText != null) this.tradeLocationText = tradeLocationText;
        if (deliveryAvailable != null) this.deliveryAvailable = deliveryAvailable;
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

    public Long getId() { return id; }
    public User getOwnerUser() { return ownerUser; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public Integer getListedPrice() { return listedPrice; }
    public String getConditionDescription() { return conditionDescription; }
    public String getDefectDescription() { return defectDescription; }
    public String getRefundPolicyText() { return refundPolicyText; }
    public String getTradeLocationText() { return tradeLocationText; }
    public boolean isDeliveryAvailable() { return deliveryAvailable; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
