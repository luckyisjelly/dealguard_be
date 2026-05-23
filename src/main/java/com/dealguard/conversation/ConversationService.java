package com.dealguard.conversation;

import com.dealguard.conversation.dto.ConversationCreateRequest;
import com.dealguard.conversation.dto.ConversationResponse;
import com.dealguard.global.NotFoundException;
import com.dealguard.product.ProductPost;
import com.dealguard.product.ProductPostService;
import com.dealguard.user.User;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ProductPostService productPostService;

    public ConversationService(ConversationRepository conversationRepository, ProductPostService productPostService) {
        this.conversationRepository = conversationRepository;
        this.productPostService = productPostService;
    }

    @Transactional
    public ConversationResponse create(User user, ConversationCreateRequest request) {
        ProductPost post = productPostService.getEntity(request.productPostId());
        return ConversationResponse.from(conversationRepository.save(new Conversation(post, user, request.title())));
    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> list(User user) {
        return conversationRepository.findByOwnerUserOrderByCreatedAtDesc(user).stream()
                .map(ConversationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Conversation getEntity(Long id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("conversation not found"));
    }

    @Transactional(readOnly = true)
    public ConversationResponse get(Long id) {
        return ConversationResponse.from(getEntity(id));
    }

    @Transactional
    public void delete(Long id) {
        conversationRepository.delete(getEntity(id));
    }
}
