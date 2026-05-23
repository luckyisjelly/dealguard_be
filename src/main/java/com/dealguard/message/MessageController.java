package com.dealguard.message;

import com.dealguard.global.ApiResponse;
import com.dealguard.message.dto.BulkMessageCreateRequest;
import com.dealguard.message.dto.MessageCreateRequest;
import com.dealguard.message.dto.MessageResponse;
import com.dealguard.message.dto.TranscriptRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conversations/{conversationId}/messages")
@Tag(name = "메시지", description = "거래 대화방의 채팅 메시지 저장 및 조회 API")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    @Operation(summary = "메시지 단건 추가", description = "대화방에 구매자, 판매자, 시스템 중 하나의 역할로 채팅 메시지 1개를 저장합니다. sentAt을 생략하면 현재 시간이 사용됩니다.")
    public ApiResponse<MessageResponse> add(@PathVariable Long conversationId, @Valid @RequestBody MessageCreateRequest request) {
        return ApiResponse.ok(messageService.add(conversationId, request));
    }

    @PostMapping("/bulk")
    @Operation(summary = "메시지 여러 개 추가", description = "대화방에 여러 개의 채팅 메시지를 한 번에 저장합니다. Swagger 테스트 시 거래 흐름을 빠르게 만들 때 사용합니다.")
    public ApiResponse<List<MessageResponse>> addBulk(@PathVariable Long conversationId,
            @Valid @RequestBody BulkMessageCreateRequest request) {
        return ApiResponse.ok(messageService.addBulk(conversationId, request));
    }

    @PostMapping("/from-transcript")
    @Operation(summary = "Transcript로 메시지 추가", description = "구매자:, 판매자:, buyer:, seller: prefix가 포함된 원문 transcript를 파싱해 메시지 목록으로 저장합니다.")
    public ApiResponse<List<MessageResponse>> addFromTranscript(@PathVariable Long conversationId,
            @Valid @RequestBody TranscriptRequest request) {
        return ApiResponse.ok(messageService.addFromTranscript(conversationId, request));
    }

    @GetMapping
    @Operation(summary = "메시지 목록 조회", description = "대화방에 저장된 채팅 메시지를 sequence 오름차순으로 조회합니다.")
    public ApiResponse<List<MessageResponse>> list(@PathVariable Long conversationId) {
        return ApiResponse.ok(messageService.list(conversationId));
    }
}
