package com.dealguard.message;

import com.dealguard.analysis.TranscriptParser;
import com.dealguard.conversation.Conversation;
import com.dealguard.conversation.ConversationService;
import com.dealguard.message.dto.BulkMessageCreateRequest;
import com.dealguard.message.dto.MessageCreateRequest;
import com.dealguard.message.dto.MessageResponse;
import com.dealguard.message.dto.TranscriptRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ConversationService conversationService;
    private final TranscriptParser transcriptParser;

    public MessageService(ChatMessageRepository chatMessageRepository, ConversationService conversationService,
            TranscriptParser transcriptParser) {
        this.chatMessageRepository = chatMessageRepository;
        this.conversationService = conversationService;
        this.transcriptParser = transcriptParser;
    }

    @Transactional
    public MessageResponse add(Long conversationId, MessageCreateRequest request) {
        return addAll(conversationId, List.of(request)).get(0);
    }

    @Transactional
    public List<MessageResponse> addBulk(Long conversationId, BulkMessageCreateRequest request) {
        return addAll(conversationId, request.messages());
    }

    @Transactional
    public List<MessageResponse> addFromTranscript(Long conversationId, TranscriptRequest request) {
        return addAll(conversationId, transcriptParser.parse(request.transcript()));
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> list(Long conversationId) {
        Conversation conversation = conversationService.getEntity(conversationId);
        return chatMessageRepository.findByConversationOrderBySequenceAsc(conversation).stream()
                .map(MessageResponse::from)
                .toList();
    }

    private List<MessageResponse> addAll(Long conversationId, List<MessageCreateRequest> requests) {
        Conversation conversation = conversationService.getEntity(conversationId);
        int nextSequence = chatMessageRepository.countByConversation(conversation) + 1;
        List<ChatMessage> messages = new ArrayList<>();
        for (MessageCreateRequest request : requests) {
            messages.add(new ChatMessage(
                    conversation,
                    request.senderRole(),
                    request.content(),
                    request.sentAt(),
                    nextSequence++));
        }
        return chatMessageRepository.saveAll(messages).stream()
                .map(MessageResponse::from)
                .toList();
    }
}
