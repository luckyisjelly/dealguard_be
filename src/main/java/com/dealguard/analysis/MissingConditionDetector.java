package com.dealguard.analysis;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MissingConditionDetector {

    public List<String> detect(ExtractedTradeCondition condition) {
        List<String> fields = new ArrayList<>();
        if (condition.price() == null) fields.add("price");
        if (isBlank(condition.refundPolicy())) fields.add("refundPolicy");
        if (isBlank(condition.productCondition())) fields.add("productCondition");
        if (condition.tradeMethod() == null || condition.tradeMethod() == TradeMethod.UNKNOWN) fields.add("tradeMethod");
        if (condition.paymentMethod() == null || condition.paymentMethod() == PaymentMethod.UNKNOWN) fields.add("paymentMethod");
        return fields;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
