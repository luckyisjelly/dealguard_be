package com.dealguard.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AmbiguousExpressionDetectorTest {

    private final AmbiguousExpressionDetector detector = new AmbiguousExpressionDetector();

    @Test
    void detectsAmbiguousExpressions() {
        assertThat(detector.detect("거의 새거고 상태 좋아요"))
                .contains("거의 새거", "상태 좋아요");
    }
}
