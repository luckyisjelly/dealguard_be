package com.dealguard.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import com.dealguard.message.SenderRole;
import org.junit.jupiter.api.Test;

class TranscriptParserTest {

    private final TranscriptParser parser = new TranscriptParser();

    @Test
    void parsesKoreanAndEnglishSpeakerPrefixes() {
        var messages = parser.parse("""
                구매자: 10만원 가능할까요?
                seller: 직거래 가능합니다
                memo without role
                """);

        assertThat(messages).hasSize(3);
        assertThat(messages.get(0).senderRole()).isEqualTo(SenderRole.BUYER);
        assertThat(messages.get(1).senderRole()).isEqualTo(SenderRole.SELLER);
        assertThat(messages.get(2).senderRole()).isEqualTo(SenderRole.SYSTEM);
    }
}
