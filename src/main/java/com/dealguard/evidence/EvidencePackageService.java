package com.dealguard.evidence;

import com.dealguard.analysis.AnalysisAlert;
import com.dealguard.analysis.AnalysisAlertRepository;
import com.dealguard.analysis.TradeConditionSnapshot;
import com.dealguard.analysis.TradeConditionSnapshotRepository;
import com.dealguard.conversation.Conversation;
import com.dealguard.conversation.ConversationService;
import com.dealguard.evidence.dto.EvidencePackageResponse;
import com.dealguard.global.BadRequestException;
import com.dealguard.global.NotFoundException;
import com.dealguard.message.ChatMessage;
import com.dealguard.message.ChatMessageRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvidencePackageService {

    private static final String DISCLAIMER =
            "This document is a structured reference based on transaction records and does not replace legal judgment.";

    private final ConversationService conversationService;
    private final EvidencePackageRepository evidencePackageRepository;
    private final TradeConditionSnapshotRepository snapshotRepository;
    private final AnalysisAlertRepository alertRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final Path evidenceDir;

    public EvidencePackageService(ConversationService conversationService,
            EvidencePackageRepository evidencePackageRepository,
            TradeConditionSnapshotRepository snapshotRepository,
            AnalysisAlertRepository alertRepository,
            ChatMessageRepository chatMessageRepository,
            @Value("${storage.evidence-dir}") String evidenceDir) {
        this.conversationService = conversationService;
        this.evidencePackageRepository = evidencePackageRepository;
        this.snapshotRepository = snapshotRepository;
        this.alertRepository = alertRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.evidenceDir = Path.of(evidenceDir);
    }

    @Transactional
    public EvidencePackageResponse generate(Long conversationId) {
        Conversation conversation = conversationService.getEntity(conversationId);
        TradeConditionSnapshot snapshot = snapshotRepository.findFirstByConversationOrderByCreatedAtDesc(conversation)
                .orElseThrow(() -> new BadRequestException("run analysis before generating evidence package"));
        List<AnalysisAlert> alerts = alertRepository.findByConversationOrderByCreatedAtDesc(conversation);
        List<ChatMessage> messages = chatMessageRepository.findByConversationOrderBySequenceAsc(conversation);

        try {
            Files.createDirectories(evidenceDir);
            byte[] pdfBytes = renderPdf(conversation, snapshot, alerts, messages);
            String hash = sha256(pdfBytes);
            String fileName = "dealguard-evidence-" + conversation.getId() + "-" + System.currentTimeMillis() + ".pdf";
            Path filePath = evidenceDir.resolve(fileName);
            Files.write(filePath, pdfBytes);
            EvidencePackage saved = evidencePackageRepository.save(new EvidencePackage(
                    conversation,
                    fileName,
                    filePath.toString(),
                    hash));
            return EvidencePackageResponse.from(saved);
        } catch (Exception ex) {
            throw new BadRequestException("failed to generate evidence package: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<EvidencePackageResponse> list(Long conversationId) {
        Conversation conversation = conversationService.getEntity(conversationId);
        return evidencePackageRepository.findByConversationOrderByGeneratedAtDesc(conversation).stream()
                .map(EvidencePackageResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public FileSystemResource pdf(Long evidencePackageId) {
        EvidencePackage evidencePackage = evidencePackageRepository.findById(evidencePackageId)
                .orElseThrow(() -> new NotFoundException("evidence package not found"));
        Path path = Path.of(evidencePackage.getFilePath());
        if (!Files.exists(path)) {
            throw new NotFoundException("pdf file not found");
        }
        return new FileSystemResource(path);
    }

    @Transactional(readOnly = true)
    public EvidencePackage getEntity(Long evidencePackageId) {
        return evidencePackageRepository.findById(evidencePackageId)
                .orElseThrow(() -> new NotFoundException("evidence package not found"));
    }

    private byte[] renderPdf(Conversation conversation, TradeConditionSnapshot snapshot,
            List<AnalysisAlert> alerts, List<ChatMessage> messages) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Font headingFont = new Font(Font.HELVETICA, 13, Font.BOLD);
        document.add(new Paragraph("DealGuard Evidence Package", titleFont));
        document.add(new Paragraph("Generated at: " + LocalDateTime.now()));
        document.add(new Paragraph(DISCLAIMER));
        document.add(new Paragraph(" "));

        addHeading(document, "Product Post Snapshot", headingFont);
        document.add(new Paragraph("Title: " + conversation.getProductPost().getTitle()));
        document.add(new Paragraph("Listed price: " + conversation.getProductPost().getListedPrice()));
        document.add(new Paragraph("Condition: " + conversation.getProductPost().getConditionDescription()));
        document.add(new Paragraph("Refund policy: " + conversation.getProductPost().getRefundPolicyText()));

        addHeading(document, "Final Extracted Trade Condition Summary", headingFont);
        document.add(new Paragraph("Price: " + snapshot.getPrice()));
        document.add(new Paragraph("Trade method: " + snapshot.getTradeMethod()));
        document.add(new Paragraph("Payment method: " + snapshot.getPaymentMethod()));
        document.add(new Paragraph("Product condition: " + snapshot.getProductCondition()));
        document.add(new Paragraph("Defects: " + snapshot.getDefectDetails()));
        document.add(new Paragraph("Refund policy: " + snapshot.getRefundPolicy()));
        document.add(new Paragraph("Confidence: " + snapshot.getConfidenceScore()));

        addHeading(document, "Analysis Alerts", headingFont);
        for (AnalysisAlert alert : alerts) {
            document.add(new Paragraph("[%s/%s] %s before=%s after=%s".formatted(
                    alert.getAlertType(),
                    alert.getSeverity(),
                    alert.getMessage(),
                    alert.getBeforeValue(),
                    alert.getAfterValue())));
        }

        addHeading(document, "Original Chat Message Timeline", headingFont);
        for (ChatMessage message : messages) {
            document.add(new Paragraph("#%d %s: %s".formatted(
                    message.getSequence(),
                    message.getSenderRole(),
                    message.getContent())));
        }

        document.close();
        return output.toByteArray();
    }

    private void addHeading(Document document, String text, Font font) throws Exception {
        document.add(new Paragraph(" "));
        document.add(new Paragraph(text, font));
    }

    private String sha256(byte[] bytes) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return HexFormat.of().formatHex(digest.digest(bytes));
    }
}
