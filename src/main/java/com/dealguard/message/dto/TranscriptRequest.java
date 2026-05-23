package com.dealguard.message.dto;

import jakarta.validation.constraints.NotBlank;

public record TranscriptRequest(@NotBlank String transcript) {
}
