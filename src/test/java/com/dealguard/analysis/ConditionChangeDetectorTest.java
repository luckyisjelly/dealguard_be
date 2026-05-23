package com.dealguard.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ConditionChangeDetectorTest {

    private final ConditionChangeDetector detector = new ConditionChangeDetector();

    @Test
    void detectsPriceChange() {
        TradeConditionSnapshot previous = new TradeConditionSnapshot(
                null, 100000, null, null, TradeMethod.DIRECT, null, PaymentMethod.CASH,
                null, null, null, null, "", 0.5, "1");
        ExtractedTradeCondition current = new ExtractedTradeCondition(
                110000, null, null, TradeMethod.DIRECT, null, PaymentMethod.CASH,
                null, null, null, null, List.of(), 0.3, List.of());

        assertThat(detector.detect(previous, current))
                .anySatisfy(change -> {
                    assertThat(change.fieldName()).isEqualTo("price");
                    assertThat(change.beforeValue()).isEqualTo("100000");
                    assertThat(change.afterValue()).isEqualTo("110000");
                });
    }
}
