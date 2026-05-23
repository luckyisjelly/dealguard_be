package com.dealguard.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class MissingConditionDetectorTest {

    private final MissingConditionDetector detector = new MissingConditionDetector();

    @Test
    void detectsMissingRefundPolicyAndProductCondition() {
        ExtractedTradeCondition condition = new ExtractedTradeCondition(
                100000,
                null,
                null,
                TradeMethod.DIRECT,
                null,
                PaymentMethod.BANK_TRANSFER,
                null,
                null,
                null,
                null,
                List.of(),
                0.5,
                List.of(1L));

        assertThat(detector.detect(condition)).contains("refundPolicy", "productCondition");
    }
}
