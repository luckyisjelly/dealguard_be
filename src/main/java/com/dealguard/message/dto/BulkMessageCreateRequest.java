package com.dealguard.message.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record BulkMessageCreateRequest(@Valid @NotEmpty List<MessageCreateRequest> messages) {
}
