package com.dealguard.analysis;

import org.springframework.stereotype.Component;

@Component
public class TradeMethodExtractor {

    public TradeMethod extract(String text) {
        String value = text == null ? "" : text;
        if (value.contains("반값택배")) return TradeMethod.HALF_DELIVERY;
        if (value.contains("퀵")) return TradeMethod.QUICK;
        if (value.contains("택배") || value.contains("편의점택배")) return TradeMethod.DELIVERY;
        if (value.contains("직거래")) return TradeMethod.DIRECT;
        return TradeMethod.UNKNOWN;
    }
}
