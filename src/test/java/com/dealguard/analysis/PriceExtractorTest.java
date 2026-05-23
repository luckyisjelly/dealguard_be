package com.dealguard.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PriceExtractorTest {

    private final PriceExtractor extractor = new PriceExtractor();

    @Test
    void extractsKoreanManWon() {
        assertThat(extractor.extract("10만원")).contains(100000);
        assertThat(extractor.extract("11만원")).contains(110000);
    }

    @Test
    void extractsCommaWonAndFreeSharing() {
        assertThat(extractor.extract("100,000원")).contains(100000);
        assertThat(extractor.extract("무료나눔")).contains(0);
    }

    @Test
    void extractsManCheonWon() {
        assertThat(extractor.extract("5만 5천원")).contains(55000);
    }
}
