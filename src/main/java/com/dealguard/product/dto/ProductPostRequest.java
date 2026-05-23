package com.dealguard.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductPostRequest(
        @NotBlank String title,
        String category,
        String description,
        @Min(0) Integer listedPrice,
        String conditionDescription,
        String defectDescription,
        String refundPolicyText,
        String tradeLocationText,
        Boolean deliveryAvailable
) {
}
