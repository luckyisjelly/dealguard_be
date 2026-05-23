package com.dealguard.analysis;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ConditionChangeDetector {

    public List<DetectedChange> detect(TradeConditionSnapshot previous, ExtractedTradeCondition current) {
        List<DetectedChange> changes = new ArrayList<>();
        if (previous == null) {
            return changes;
        }
        compare("price", previous.getPrice(), current.price(), changes);
        compare("refundPolicy", previous.getRefundPolicy(), current.refundPolicy(), changes);
        compare("tradeMethod", previous.getTradeMethod(), current.tradeMethod(), changes);
        compare("paymentMethod", previous.getPaymentMethod(), current.paymentMethod(), changes);
        return changes;
    }

    private void compare(String field, Object before, Object after, List<DetectedChange> changes) {
        if (before != null && after != null && !before.equals(after)) {
            changes.add(new DetectedChange(field, String.valueOf(before), String.valueOf(after)));
        }
    }

    public record DetectedChange(String fieldName, String beforeValue, String afterValue) {
    }
}
