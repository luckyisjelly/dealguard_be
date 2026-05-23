package com.dealguard.analysis;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AmbiguousExpressionDetector {

    private static final List<String> DICTIONARY = List.of(
            "거의 새거",
            "상태 좋아요",
            "깨끗해요",
            "문제 없어요",
            "생활기스 있음",
            "사용감 있음",
            "작동 잘 돼요",
            "나중에 봐서요",
            "적당히 네고 가능",
            "쿨거래",
            "택포인지 봐야 해요",
            "환불은 좀 어려워요"
    );

    public List<String> detect(String text) {
        String value = text == null ? "" : text;
        return DICTIONARY.stream()
                .filter(value::contains)
                .toList();
    }
}
