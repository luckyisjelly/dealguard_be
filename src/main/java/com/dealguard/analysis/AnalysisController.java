package com.dealguard.analysis;

import com.dealguard.analysis.dto.AnalysisAlertResponse;
import com.dealguard.analysis.dto.AnalysisResultResponse;
import com.dealguard.analysis.dto.TradeConditionSnapshotResponse;
import com.dealguard.global.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conversations/{conversationId}")
@Tag(name = "분석", description = "채팅 메시지에서 거래 조건을 추출하고 위험 alert를 생성하는 API")
public class AnalysisController {

    private final ConversationAnalysisService conversationAnalysisService;

    public AnalysisController(ConversationAnalysisService conversationAnalysisService) {
        this.conversationAnalysisService = conversationAnalysisService;
    }

    @PostMapping("/analyze")
    @Operation(summary = "대화 분석 실행", description = "대화방의 채팅 메시지를 rule-based/mock AI 방식으로 분석합니다. 거래 조건 snapshot을 생성하고 누락 조건, 모호한 표현, 조건 변경, 게시글-채팅 불일치, 위험 결제 패턴 alert를 생성합니다.")
    public ApiResponse<AnalysisResultResponse> analyze(@PathVariable Long conversationId) {
        return ApiResponse.ok(conversationAnalysisService.analyze(conversationId));
    }

    @GetMapping("/summary")
    @Operation(summary = "최신 거래 조건 summary 조회", description = "가장 최근 분석에서 추출된 가격, 거래 방식, 결제 방식, 상품 상태, 환불 정책 등의 거래 조건 snapshot을 조회합니다.")
    public ApiResponse<TradeConditionSnapshotResponse> summary(@PathVariable Long conversationId) {
        return ApiResponse.ok(conversationAnalysisService.latestSummary(conversationId));
    }

    @GetMapping("/alerts")
    @Operation(summary = "분석 alert 목록 조회", description = "대화방에서 생성된 분석 alert 목록을 조회합니다. 누락 조건, 모호한 표현, 조건 변경, 게시글-채팅 불일치, 위험 결제 패턴을 확인할 수 있습니다.")
    public ApiResponse<List<AnalysisAlertResponse>> alerts(@PathVariable Long conversationId) {
        return ApiResponse.ok(conversationAnalysisService.alerts(conversationId));
    }

    @GetMapping("/condition-history")
    @Operation(summary = "거래 조건 히스토리 조회", description = "분석을 실행할 때마다 저장된 거래 조건 snapshot 히스토리를 조회합니다. 조건 변경 탐지 결과를 확인할 때 사용합니다.")
    public ApiResponse<List<TradeConditionSnapshotResponse>> conditionHistory(@PathVariable Long conversationId) {
        return ApiResponse.ok(conversationAnalysisService.conditionHistory(conversationId));
    }
}
