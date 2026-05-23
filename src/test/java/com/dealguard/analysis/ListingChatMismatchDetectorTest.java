package com.dealguard.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import com.dealguard.product.ProductPost;
import com.dealguard.user.User;
import java.util.List;
import org.junit.jupiter.api.Test;

class ListingChatMismatchDetectorTest {

    private final ListingChatMismatchDetector detector = new ListingChatMismatchDetector();

    @Test
    void detectsListedPriceAndChatPriceMismatch() {
        User user = new User("user@example.com", "hash", "tester");
        ProductPost post = new ProductPost(user, "아이폰", "digital", "desc", 100000,
                "거의 새거", null, null, "홍대입구역", false);
        ExtractedTradeCondition condition = new ExtractedTradeCondition(
                110000, null, null, TradeMethod.DIRECT, null, PaymentMethod.CASH,
                null, null, null, null, List.of(), 0.5, List.of());

        assertThat(detector.detect(post, condition))
                .anySatisfy(change -> {
                    assertThat(change.fieldName()).isEqualTo("price");
                    assertThat(change.beforeValue()).isEqualTo("100000");
                    assertThat(change.afterValue()).isEqualTo("110000");
                });
    }
}
