package com.dealguard.analysis;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RiskyPaymentDetector {

    private static final List<String> PATTERNS = List.of(
            "선입금",
            "예약금",
            "외부 링크",
            "밖에서 거래",
            "플랫폼 밖",
            "안전결제 말고",
            "수수료 때문에",
            "계좌 먼저",
            "송장 나중에"
    );

    public List<String> detect(String text) {
        String value = text == null ? "" : text;
        return PATTERNS.stream()
                .filter(value::contains)
                .toList();
    }
}
