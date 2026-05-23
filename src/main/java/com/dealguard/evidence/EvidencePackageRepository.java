package com.dealguard.evidence;

import com.dealguard.conversation.Conversation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvidencePackageRepository extends JpaRepository<EvidencePackage, Long> {

    List<EvidencePackage> findByConversationOrderByGeneratedAtDesc(Conversation conversation);
}
