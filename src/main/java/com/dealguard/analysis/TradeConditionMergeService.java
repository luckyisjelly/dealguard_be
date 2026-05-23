package com.dealguard.analysis;

import com.dealguard.message.ChatMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class TradeConditionMergeService {

    private final PriceExtractor priceExtractor;
    private final TradeMethodExtractor tradeMethodExtractor;
    private final PaymentMethodExtractor paymentMethodExtractor;
    private final RefundPolicyExtractor refundPolicyExtractor;

    public TradeConditionMergeService(PriceExtractor priceExtractor, TradeMethodExtractor tradeMethodExtractor,
            PaymentMethodExtractor paymentMethodExtractor, RefundPolicyExtractor refundPolicyExtractor) {
        this.priceExtractor = priceExtractor;
        this.tradeMethodExtractor = tradeMethodExtractor;
        this.paymentMethodExtractor = paymentMethodExtractor;
        this.refundPolicyExtractor = refundPolicyExtractor;
    }

    public ExtractedTradeCondition merge(List<ChatMessage> messages) {
        Integer price = null;
        TradeMethod tradeMethod = TradeMethod.UNKNOWN;
        PaymentMethod paymentMethod = PaymentMethod.UNKNOWN;
        String refundPolicy = null;
        String productCondition = null;
        String defectDetails = null;
        List<Long> sources = new ArrayList<>();

        for (ChatMessage message : messages) {
            String content = message.getContent();
            if (priceExtractor.extract(content).isPresent()) {
                price = priceExtractor.extract(content).get();
                sources.add(message.getId());
            }
            TradeMethod detectedMethod = tradeMethodExtractor.extract(content);
            if (detectedMethod != TradeMethod.UNKNOWN) tradeMethod = detectedMethod;

            PaymentMethod detectedPayment = paymentMethodExtractor.extract(content);
            if (detectedPayment != PaymentMethod.UNKNOWN) paymentMethod = detectedPayment;

            if (refundPolicyExtractor.extract(content).isPresent()) {
                refundPolicy = refundPolicyExtractor.extract(content).get();
            }
            if (containsConditionHint(content)) {
                productCondition = content;
            }
            if (content.contains("기스") || content.contains("하자") || content.contains("고장")) {
                defectDetails = content;
            }
        }

        List<Long> sourceIds = sources.stream().filter(Objects::nonNull).distinct().toList();
        return new ExtractedTradeCondition(
                price,
                null,
                null,
                tradeMethod,
                null,
                paymentMethod,
                productCondition,
                defectDetails,
                refundPolicy,
                null,
                List.of(),
                score(price, tradeMethod, paymentMethod, refundPolicy, productCondition),
                sourceIds);
    }

    private boolean containsConditionHint(String content) {
        return content.contains("상태") || content.contains("새거") || content.contains("깨끗")
                || content.contains("사용감") || content.contains("작동") || content.contains("기스");
    }

    private double score(Integer price, TradeMethod tradeMethod, PaymentMethod paymentMethod, String refundPolicy,
            String productCondition) {
        int filled = 0;
        if (price != null) filled++;
        if (tradeMethod != TradeMethod.UNKNOWN) filled++;
        if (paymentMethod != PaymentMethod.UNKNOWN) filled++;
        if (refundPolicy != null) filled++;
        if (productCondition != null) filled++;
        return filled / 5.0;
    }
}
