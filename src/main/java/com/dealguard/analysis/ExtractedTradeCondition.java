package com.dealguard.analysis;

import java.util.List;

public record ExtractedTradeCondition(
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
        List<String> includedItems,
        double confidenceScore,
        List<Long> sourceMessageIds
) {
}
