# Domain Model

## User

Stores authentication and ownership information.

Fields: id, email, passwordHash, nickname, role, createdAt, updatedAt.

## ProductPost

Stores a seller-provided listing snapshot.

Fields: id, ownerUser, title, category, description, listedPrice, conditionDescription, defectDescription, refundPolicyText, tradeLocationText, deliveryAvailable, createdAt, updatedAt.

## Conversation

Represents one transaction negotiation linked to a product post.

Fields: id, productPost, ownerUser, title, status, createdAt, updatedAt.

## ChatMessage

Stores individual chat lines.

Fields: id, conversation, senderRole, content, sentAt, sequence, createdAt.

## TradeConditionSnapshot

Stores extracted transaction conditions at an analysis point.

Fields: id, conversation, price, place, tradeTimeText, tradeMethod, deliveryFeePolicy, paymentMethod, productCondition, defectDetails, refundPolicy, negotiationPolicy, includedItems, confidenceScore, sourceMessageIds, createdAt.

## AnalysisAlert

Stores detected risks and changes.

Fields: id, conversation, alertType, severity, fieldName, message, beforeValue, afterValue, sourceMessageIds, resolved, createdAt.

## EvidencePackage

Stores generated PDF metadata.

Fields: id, conversation, fileName, filePath, sha256Hash, generatedAt.

## AiAnalysisLog

Stores external or mock AI request/response summaries.

Fields: id, conversation, provider, requestSummary, responseSummary, status, createdAt.
