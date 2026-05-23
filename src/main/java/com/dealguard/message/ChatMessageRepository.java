package com.dealguard.message;

import com.dealguard.conversation.Conversation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByConversationOrderBySequenceAsc(Conversation conversation);

    int countByConversation(Conversation conversation);
}
