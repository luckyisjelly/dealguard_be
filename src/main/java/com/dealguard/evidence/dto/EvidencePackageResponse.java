package com.dealguard.evidence.dto;

import com.dealguard.evidence.EvidencePackage;
import java.time.LocalDateTime;

public record EvidencePackageResponse(
        Long id,
        Long conversationId,
        String fileName,
        String sha256Hash,
        LocalDateTime generatedAt
) {
    public static EvidencePackageResponse from(EvidencePackage evidencePackage) {
        return new EvidencePackageResponse(
                evidencePackage.getId(),
                evidencePackage.getConversation().getId(),
                evidencePackage.getFileName(),
                evidencePackage.getSha256Hash(),
                evidencePackage.getGeneratedAt());
    }
}
