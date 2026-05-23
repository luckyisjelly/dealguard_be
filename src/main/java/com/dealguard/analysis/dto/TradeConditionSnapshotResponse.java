package com.dealguard.analysis.dto;

import com.dealguard.analysis.PaymentMethod;
import com.dealguard.analysis.TradeConditionSnapshot;
import com.dealguard.analysis.TradeMethod;
import java.time.LocalDateTime;

public record TradeConditionSnapshotResponse(
        Long id,
        Long conversationId,
        Integer price,
        String place,
        String tradeTimeText,
        TradeMethod tradeMethod,
        String deliveryFeePolicy,
        PaymentMethod paymentMethod,
        String productCondition,
        String defectDetails,
        String refundPolicy,
        String negotiationPolicy,
        String includedItems,
        Double confidenceScore,
        String sourceMessageIds,
        LocalDateTime createdAt
) {
    public static TradeConditionSnapshotResponse from(TradeConditionSnapshot snapshot) {
        return new TradeConditionSnapshotResponse(
                snapshot.getId(),
                snapshot.getConversation().getId(),
                snapshot.getPrice(),
                snapshot.getPlace(),
                snapshot.getTradeTimeText(),
                snapshot.getTradeMethod(),
                snapshot.getDeliveryFeePolicy(),
                snapshot.getPaymentMethod(),
                snapshot.getProductCondition(),
                snapshot.getDefectDetails(),
                snapshot.getRefundPolicy(),
                snapshot.getNegotiationPolicy(),
                snapshot.getIncludedItems(),
                snapshot.getConfidenceScore(),
                snapshot.getSourceMessageIds(),
                snapshot.getCreatedAt());
    }
}
