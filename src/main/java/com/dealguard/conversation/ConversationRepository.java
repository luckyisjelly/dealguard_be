package com.dealguard.conversation;

import com.dealguard.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findByOwnerUserOrderByCreatedAtDesc(User ownerUser);
}
