# AI Analysis Design

The first milestone uses deterministic rule-based analysis. This keeps the backend runnable without API keys and makes behavior easy to present.

## Components

- `PriceExtractor`
- `TradeMethodExtractor`
- `PaymentMethodExtractor`
- `RefundPolicyExtractor`
- `AmbiguousExpressionDetector`
- `MissingConditionDetector`
- `ConditionChangeDetector`
- `ListingChatMismatchDetector`
- `RiskyPaymentDetector`
- `TranscriptParser`
- `TradeConditionMergeService`
- `ConversationAnalysisService`

## AI Client Interface

`AiAnalysisClient` hides the provider implementation.

Implementations:

- `MockAiAnalysisClient`
- `GeminiAiAnalysisClient`

Configuration:

```properties
ai.provider=mock
ai.gemini.api-key=${GEMINI_API_KEY:}
ai.gemini.model=gemini-2.5-flash-lite
```

If `ai.provider=gemini` and the API key is missing, the client fails gracefully with a clear error.

## Future Structured JSON

The future LLM response should return structured fields such as price, place, trade method, payment method, product condition, defects, refund policy, included items, and ambiguous expressions.
