package com.dealguard.analysis;

import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class RefundPolicyExtractor {

    public Optional<String> extract(String text) {
        String value = text == null ? "" : text;
        if (value.contains("환불 불가") || value.contains("교환 환불 안됨") || value.contains("환불 어려워요")) {
            return Optional.of("환불 불가");
        }
        if (value.contains("환불 가능") || value.contains("반품 가능") || value.contains("교환 가능")) {
            return Optional.of("환불 가능");
        }
        return Optional.empty();
    }
}
