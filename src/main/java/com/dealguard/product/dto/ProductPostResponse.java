package com.dealguard.product.dto;

import com.dealguard.product.ProductPost;
import java.time.LocalDateTime;

public record ProductPostResponse(
        Long id,
        Long ownerUserId,
        String title,
        String category,
        String description,
        Integer listedPrice,
        String conditionDescription,
        String defectDescription,
        String refundPolicyText,
        String tradeLocationText,
        boolean deliveryAvailable,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductPostResponse from(ProductPost post) {
        return new ProductPostResponse(
                post.getId(),
                post.getOwnerUser().getId(),
                post.getTitle(),
                post.getCategory(),
                post.getDescription(),
                post.getListedPrice(),
                post.getConditionDescription(),
                post.getDefectDescription(),
                post.getRefundPolicyText(),
                post.getTradeLocationText(),
                post.isDeliveryAvailable(),
                post.getCreatedAt(),
                post.getUpdatedAt());
    }
}
