package com.dealguard.ai;

import com.dealguard.analysis.ExtractedTradeCondition;
import com.dealguard.analysis.TradeConditionMergeService;
import com.dealguard.message.ChatMessage;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "mock", matchIfMissing = true)
public class MockAiAnalysisClient implements AiAnalysisClient {

    private final TradeConditionMergeService tradeConditionMergeService;

    public MockAiAnalysisClient(TradeConditionMergeService tradeConditionMergeService) {
        this.tradeConditionMergeService = tradeConditionMergeService;
    }

    @Override
    public ExtractedTradeCondition analyze(List<ChatMessage> messages) {
        return tradeConditionMergeService.merge(messages);
    }
}
