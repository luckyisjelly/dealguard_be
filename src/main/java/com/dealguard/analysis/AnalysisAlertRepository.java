package com.dealguard.analysis;

import com.dealguard.conversation.Conversation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisAlertRepository extends JpaRepository<AnalysisAlert, Long> {

    List<AnalysisAlert> findByConversationOrderByCreatedAtDesc(Conversation conversation);
}
