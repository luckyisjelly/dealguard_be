package com.dealguard.analysis;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class PriceExtractor {

    private static final Pattern COMMA_WON = Pattern.compile("(\\d{1,3}(?:,\\d{3})+)\\s*원");
    private static final Pattern MAN_CHEON = Pattern.compile("(\\d+)\\s*만\\s*(\\d+)\\s*천\\s*원?");
    private static final Pattern MAN = Pattern.compile("(\\d+)\\s*만\\s*원?");
    private static final Pattern WON = Pattern.compile("(\\d+)\\s*원");

    public Optional<Integer> extract(String text) {
        if (text == null || text.isBlank()) {
            return Optional.empty();
        }
        if (text.contains("무료나눔")) {
            return Optional.of(0);
        }
        Matcher comma = COMMA_WON.matcher(text);
        if (comma.find()) {
            return Optional.of(Integer.parseInt(comma.group(1).replace(",", "")));
        }
        Matcher manCheon = MAN_CHEON.matcher(text);
        if (manCheon.find()) {
            return Optional.of(Integer.parseInt(manCheon.group(1)) * 10000
                    + Integer.parseInt(manCheon.group(2)) * 1000);
        }
        Matcher man = MAN.matcher(text);
        if (man.find()) {
            return Optional.of(Integer.parseInt(man.group(1)) * 10000);
        }
        Matcher won = WON.matcher(text);
        if (won.find()) {
            return Optional.of(Integer.parseInt(won.group(1)));
        }
        return Optional.empty();
    }
}
