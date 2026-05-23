package com.dealguard.analysis;

import com.dealguard.product.ProductPost;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ListingChatMismatchDetector {

    public List<ConditionChangeDetector.DetectedChange> detect(ProductPost post, ExtractedTradeCondition condition) {
        List<ConditionChangeDetector.DetectedChange> mismatches = new ArrayList<>();
        if (post.getListedPrice() != null && condition.price() != null && !post.getListedPrice().equals(condition.price())) {
            mismatches.add(new ConditionChangeDetector.DetectedChange(
                    "price",
                    String.valueOf(post.getListedPrice()),
                    String.valueOf(condition.price())));
        }
        if (hasText(post.getRefundPolicyText()) && hasText(condition.refundPolicy())
                && !post.getRefundPolicyText().contains(condition.refundPolicy())) {
            mismatches.add(new ConditionChangeDetector.DetectedChange(
                    "refundPolicy",
                    post.getRefundPolicyText(),
                    condition.refundPolicy()));
        }
        return mismatches;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
