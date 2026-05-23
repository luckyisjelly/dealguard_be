package com.dealguard.analysis;

import org.springframework.stereotype.Component;

@Component
public class PaymentMethodExtractor {

    public PaymentMethod extract(String text) {
        String value = text == null ? "" : text;
        if (value.contains("안전결제")) return PaymentMethod.SAFE_PAYMENT;
        if (value.contains("계좌") || value.contains("송금") || value.contains("이체") || value.contains("입금")) {
            return PaymentMethod.BANK_TRANSFER;
        }
        if (value.contains("현금")) return PaymentMethod.CASH;
        return PaymentMethod.UNKNOWN;
    }
}
