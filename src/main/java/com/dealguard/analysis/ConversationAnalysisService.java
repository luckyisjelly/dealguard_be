package com.dealguard.analysis;

import com.dealguard.ai.AiAnalysisClient;
import com.dealguard.analysis.ConditionChangeDetector.DetectedChange;
import com.dealguard.analysis.dto.AnalysisAlertResponse;
import com.dealguard.analysis.dto.AnalysisResultResponse;
import com.dealguard.analysis.dto.TradeConditionSnapshotResponse;
import com.dealguard.conversation.Conversation;
import com.dealguard.conversation.ConversationService;
import com.dealguard.message.ChatMessage;
import com.dealguard.message.ChatMessageRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConversationAnalysisService {

    private final ConversationService conversationService;
    private final ChatMessageRepository chatMessageRepository;
    private final TradeConditionSnapshotRepository snapshotRepository;
    private final AnalysisAlertRepository alertRepository;
    private final AiAnalysisClient aiAnalysisClient;
    private final MissingConditionDetector missingConditionDetector;
    private final ConditionChangeDetector conditionChangeDetector;
    private final ListingChatMismatchDetector listingChatMismatchDetector;
    private final AmbiguousExpressionDetector ambiguousExpressionDetector;
    private final RiskyPaymentDetector riskyPaymentDetector;

    public ConversationAnalysisService(ConversationService conversationService,
            ChatMessageRepository chatMessageRepository,
            TradeConditionSnapshotRepository snapshotRepository,
            AnalysisAlertRepository alertRepository,
            AiAnalysisClient aiAnalysisClient,
            MissingConditionDetector missingConditionDetector,
            ConditionChangeDetector conditionChangeDetector,
            ListingChatMismatchDetector listingChatMismatchDetector,
            AmbiguousExpressionDetector ambiguousExpressionDetector,
            RiskyPaymentDetector riskyPaymentDetector) {
        this.conversationService = conversationService;
        this.chatMessageRepository = chatMessageRepository;
        this.snapshotRepository = snapshotRepository;
        this.alertRepository = alertRepository;
        this.aiAnalysisClient = aiAnalysisClient;
        this.missingConditionDetector = missingConditionDetector;
        this.conditionChangeDetector = conditionChangeDetector;
        this.listingChatMismatchDetector = listingChatMismatchDetector;
        this.ambiguousExpressionDetector = ambiguousExpressionDetector;
        this.riskyPaymentDetector = riskyPaymentDetector;
    }

    @Transactional
    public AnalysisResultResponse analyze(Long conversationId) {
        Conversation conversation = conversationService.getEntity(conversationId);
        List<ChatMessage> messages = chatMessageRepository.findByConversationOrderBySequenceAsc(conversation);
        TradeConditionSnapshot previous = snapshotRepository.findFirstByConversationOrderByCreatedAtDesc(conversation).orElse(null);
        ExtractedTradeCondition condition = aiAnalysisClient.analyze(messages);

        TradeConditionSnapshot snapshot = snapshotRepository.save(toSnapshot(conversation, condition));
        List<AnalysisAlert> alerts = new ArrayList<>();
        alerts.addAll(createMissingAlerts(conversation, condition));
        alerts.addAll(createChangeAlerts(conversation, previous, condition));
        alerts.addAll(createMismatchAlerts(conversation, condition));
        alerts.addAll(createTextPatternAlerts(conversation, messages));
        List<AnalysisAlert> savedAlerts = alertRepository.saveAll(alerts);

        return new AnalysisResultResponse(
                TradeConditionSnapshotResponse.from(snapshot),
                savedAlerts.stream().map(AnalysisAlertResponse::from).toList());
    }

    @Transactional(readOnly = true)
    public TradeConditionSnapshotResponse latestSummary(Long conversationId) {
        Conversation conversation = conversationService.getEntity(conversationId);
        return snapshotRepository.findFirstByConversationOrderByCreatedAtDesc(conversation)
                .map(TradeConditionSnapshotResponse::from)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<AnalysisAlertResponse> alerts(Long conversationId) {
        Conversation conversation = conversationService.getEntity(conversationId);
        return alertRepository.findByConversationOrderByCreatedAtDesc(conversation).stream()
                .map(AnalysisAlertResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TradeConditionSnapshotResponse> conditionHistory(Long conversationId) {
        Conversation conversation = conversationService.getEntity(conversationId);
        return snapshotRepository.findByConversationOrderByCreatedAtDesc(conversation).stream()
                .map(TradeConditionSnapshotResponse::from)
                .toList();
    }

    private TradeConditionSnapshot toSnapshot(Conversation conversation, ExtractedTradeCondition condition) {
        return new TradeConditionSnapshot(
                conversation,
                condition.price(),
                condition.place(),
                condition.tradeTimeText(),
                condition.tradeMethod(),
                condition.deliveryFeePolicy(),
                condition.paymentMethod(),
                condition.productCondition(),
                condition.defectDetails(),
                condition.refundPolicy(),
                condition.negotiationPolicy(),
                String.join(", ", condition.includedItems()),
                condition.confidenceScore(),
                joinIds(condition.sourceMessageIds()));
    }

    private List<AnalysisAlert> createMissingAlerts(Conversation conversation, ExtractedTradeCondition condition) {
        return missingConditionDetector.detect(condition).stream()
                .map(field -> new AnalysisAlert(
                        conversation,
                        AlertType.MISSING_CONDITION,
                        AlertSeverity.MEDIUM,
                        field,
                        "Missing transaction condition: " + field,
                        null,
                        null,
                        joinIds(condition.sourceMessageIds())))
                .toList();
    }

    private List<AnalysisAlert> createChangeAlerts(Conversation conversation, TradeConditionSnapshot previous,
            ExtractedTradeCondition condition) {
        return conditionChangeDetector.detect(previous, condition).stream()
                .map(change -> new AnalysisAlert(
                        conversation,
                        AlertType.CONDITION_CHANGED,
                        AlertSeverity.HIGH,
                        change.fieldName(),
                        "Condition changed: " + change.fieldName(),
                        change.beforeValue(),
                        change.afterValue(),
                        joinIds(condition.sourceMessageIds())))
                .toList();
    }

    private List<AnalysisAlert> createMismatchAlerts(Conversation conversation, ExtractedTradeCondition condition) {
        return listingChatMismatchDetector.detect(conversation.getProductPost(), condition).stream()
                .map(change -> new AnalysisAlert(
                        conversation,
                        AlertType.LISTING_CHAT_MISMATCH,
                        AlertSeverity.HIGH,
                        change.fieldName(),
                        "Listing and chat values differ: " + change.fieldName(),
                        change.beforeValue(),
                        change.afterValue(),
                        joinIds(condition.sourceMessageIds())))
                .toList();
    }

    private List<AnalysisAlert> createTextPatternAlerts(Conversation conversation, List<ChatMessage> messages) {
        List<AnalysisAlert> alerts = new ArrayList<>();
        for (ChatMessage message : messages) {
            for (String expression : ambiguousExpressionDetector.detect(message.getContent())) {
                alerts.add(new AnalysisAlert(
                        conversation,
                        AlertType.AMBIGUOUS_EXPRESSION,
                        AlertSeverity.MEDIUM,
                        "content",
                        "Ambiguous expression detected: " + expression,
                        null,
                        expression,
                        String.valueOf(message.getId())));
            }
            for (String pattern : riskyPaymentDetector.detect(message.getContent())) {
                alerts.add(new AnalysisAlert(
                        conversation,
                        AlertType.RISKY_PAYMENT,
                        AlertSeverity.HIGH,
                        "content",
                        "Risky payment or trade pattern detected: " + pattern,
                        null,
                        pattern,
                        String.valueOf(message.getId())));
            }
        }
        return alerts;
    }

    private String joinIds(List<Long> ids) {
        return ids == null ? "" : ids.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
