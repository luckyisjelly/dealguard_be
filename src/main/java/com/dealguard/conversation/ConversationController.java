package com.dealguard.conversation;

import com.dealguard.conversation.dto.ConversationCreateRequest;
import com.dealguard.conversation.dto.ConversationResponse;
import com.dealguard.global.ApiResponse;
import com.dealguard.global.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conversations")
@Tag(name = "대화방", description = "상품 게시글에 연결된 거래 대화방 관리 API")
public class ConversationController {

    private final ConversationService conversationService;
    private final SecurityUtil securityUtil;

    public ConversationController(ConversationService conversationService, SecurityUtil securityUtil) {
        this.conversationService = conversationService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    @Operation(summary = "대화방 생성", description = "특정 상품 게시글에 연결된 거래 대화방을 생성합니다. 이후 이 대화방에 채팅 메시지를 저장하고 분석을 실행합니다.")
    public ApiResponse<ConversationResponse> create(@Valid @RequestBody ConversationCreateRequest request) {
        return ApiResponse.ok(conversationService.create(securityUtil.currentUser(), request));
    }

    @GetMapping
    @Operation(summary = "내 대화방 목록 조회", description = "현재 로그인한 사용자가 생성한 거래 대화방 목록을 최신순으로 조회합니다.")
    public ApiResponse<List<ConversationResponse>> list() {
        return ApiResponse.ok(conversationService.list(securityUtil.currentUser()));
    }

    @GetMapping("/{conversationId}")
    @Operation(summary = "대화방 상세 조회", description = "대화방 ID로 거래 대화방의 기본 정보를 조회합니다.")
    public ApiResponse<ConversationResponse> get(@PathVariable Long conversationId) {
        return ApiResponse.ok(conversationService.get(conversationId));
    }

    @DeleteMapping("/{conversationId}")
    @Operation(summary = "대화방 삭제", description = "대화방 ID에 해당하는 거래 대화방을 삭제합니다.")
    public ApiResponse<Void> delete(@PathVariable Long conversationId) {
        conversationService.delete(conversationId);
        return ApiResponse.ok();
    }
}
