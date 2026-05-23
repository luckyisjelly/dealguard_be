package com.dealguard.analysis;

import com.dealguard.conversation.Conversation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeConditionSnapshotRepository extends JpaRepository<TradeConditionSnapshot, Long> {

    List<TradeConditionSnapshot> findByConversationOrderByCreatedAtDesc(Conversation conversation);

    Optional<TradeConditionSnapshot> findFirstByConversationOrderByCreatedAtDesc(Conversation conversation);
}
