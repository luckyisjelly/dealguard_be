package com.dealguard.analysis.dto;

import java.util.List;

public record AnalysisResultResponse(
        TradeConditionSnapshotResponse summary,
        List<AnalysisAlertResponse> alerts
) {
}
