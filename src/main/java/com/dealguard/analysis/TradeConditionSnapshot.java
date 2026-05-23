package com.dealguard.analysis;

import com.dealguard.conversation.Conversation;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TradeConditionSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    private Integer price;
    private String place;
    @Column(name = "trade_time_text")
    private String tradeTimeText;

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_method")
    private TradeMethod tradeMethod;

    @Column(name = "delivery_fee_policy")
    private String deliveryFeePolicy;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "product_condition", columnDefinition = "text")
    private String productCondition;

    @Column(name = "defect_details", columnDefinition = "text")
    private String defectDetails;

    @Column(name = "refund_policy", columnDefinition = "text")
    private String refundPolicy;

    @Column(name = "negotiation_policy")
    private String negotiationPolicy;
    @Column(name = "included_items")
    private String includedItems;
    @Column(name = "confidence_score")
    private Double confidenceScore;
    @Column(name = "source_message_ids")
    private String sourceMessageIds;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected TradeConditionSnapshot() {
    }

    public TradeConditionSnapshot(Conversation conversation, Integer price, String place, String tradeTimeText,
            TradeMethod tradeMethod, String deliveryFeePolicy, PaymentMethod paymentMethod, String productCondition,
            String defectDetails, String refundPolicy, String negotiationPolicy, String includedItems,
            Double confidenceScore, String sourceMessageIds) {
        this.conversation = conversation;
        this.price = price;
        this.place = place;
        this.tradeTimeText = tradeTimeText;
        this.tradeMethod = tradeMethod;
        this.deliveryFeePolicy = deliveryFeePolicy;
        this.paymentMethod = paymentMethod;
        this.productCondition = productCondition;
        this.defectDetails = defectDetails;
        this.refundPolicy = refundPolicy;
        this.negotiationPolicy = negotiationPolicy;
        this.includedItems = includedItems;
        this.confidenceScore = confidenceScore;
        this.sourceMessageIds = sourceMessageIds;
    }

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Conversation getConversation() { return conversation; }
    public Integer getPrice() { return price; }
    public String getPlace() { return place; }
    public String getTradeTimeText() { return tradeTimeText; }
    public TradeMethod getTradeMethod() { return tradeMethod; }
    public String getDeliveryFeePolicy() { return deliveryFeePolicy; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public String getProductCondition() { return productCondition; }
    public String getDefectDetails() { return defectDetails; }
    public String getRefundPolicy() { return refundPolicy; }
    public String getNegotiationPolicy() { return negotiationPolicy; }
    public String getIncludedItems() { return includedItems; }
    public Double getConfidenceScore() { return confidenceScore; }
    public String getSourceMessageIds() { return sourceMessageIds; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
