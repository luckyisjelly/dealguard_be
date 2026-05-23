package com.dealguard.ai;

import com.dealguard.analysis.ExtractedTradeCondition;
import com.dealguard.global.BadRequestException;
import com.dealguard.message.ChatMessage;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "gemini")
public class GeminiAiAnalysisClient implements AiAnalysisClient {

    private final String apiKey;
    private final String model;
    private final WebClient webClient;

    public GeminiAiAnalysisClient(
            @Value("${ai.gemini.api-key}") String apiKey,
            @Value("${ai.gemini.model}") String model,
            WebClient.Builder webClientBuilder) {
        this.apiKey = apiKey;
        this.model = model;
        this.webClient = webClientBuilder.baseUrl("https://generativelanguage.googleapis.com").build();
    }

    @Override
    public ExtractedTradeCondition analyze(List<ChatMessage> messages) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new BadRequestException("GEMINI_API_KEY is required when ai.provider=gemini");
        }
        throw new BadRequestException("Gemini analysis client is prepared but not enabled for production use yet: " + model);
    }
}
