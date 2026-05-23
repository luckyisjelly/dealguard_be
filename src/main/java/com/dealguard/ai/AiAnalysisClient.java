package com.dealguard.ai;

import com.dealguard.analysis.ExtractedTradeCondition;
import com.dealguard.message.ChatMessage;
import java.util.List;

public interface AiAnalysisClient {

    ExtractedTradeCondition analyze(List<ChatMessage> messages);
}
