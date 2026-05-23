package com.dealguard.analysis;

import com.dealguard.message.SenderRole;
import com.dealguard.message.dto.MessageCreateRequest;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TranscriptParser {

    public List<MessageCreateRequest> parse(String transcript) {
        if (transcript == null || transcript.isBlank()) {
            return List.of();
        }
        return Arrays.stream(transcript.split("\\R"))
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .map(this::parseLine)
                .toList();
    }

    private MessageCreateRequest parseLine(String line) {
        String normalized = line.toLowerCase();
        if (normalized.startsWith("buyer:") || line.startsWith("구매자:")) {
            return new MessageCreateRequest(SenderRole.BUYER, afterColon(line), null);
        }
        if (normalized.startsWith("seller:") || line.startsWith("판매자:")) {
            return new MessageCreateRequest(SenderRole.SELLER, afterColon(line), null);
        }
        return new MessageCreateRequest(SenderRole.SYSTEM, line, null);
    }

    private String afterColon(String line) {
        int index = line.indexOf(':');
        return index >= 0 ? line.substring(index + 1).trim() : line;
    }
}
