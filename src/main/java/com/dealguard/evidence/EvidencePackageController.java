package com.dealguard.evidence;

import com.dealguard.evidence.dto.EvidencePackageResponse;
import com.dealguard.global.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "증거 패키지", description = "분석 결과와 원본 채팅 기록을 PDF 증거 패키지로 생성하고 다운로드하는 API")
public class EvidencePackageController {

    private final EvidencePackageService evidencePackageService;

    public EvidencePackageController(EvidencePackageService evidencePackageService) {
        this.evidencePackageService = evidencePackageService;
    }

    @PostMapping("/api/conversations/{conversationId}/evidence-packages")
    @Operation(summary = "PDF 증거 패키지 생성", description = "분석 summary, alert 목록, 상품 게시글 snapshot, 원본 채팅 타임라인을 포함한 PDF 증거 패키지를 생성합니다. 먼저 대화 분석을 실행해야 합니다.")
    public ApiResponse<EvidencePackageResponse> generate(@PathVariable Long conversationId) {
        return ApiResponse.ok(evidencePackageService.generate(conversationId));
    }

    @GetMapping("/api/conversations/{conversationId}/evidence-packages")
    @Operation(summary = "PDF 증거 패키지 목록 조회", description = "대화방별로 생성된 PDF 증거 패키지 목록과 파일명, SHA-256 해시, 생성 시각을 조회합니다.")
    public ApiResponse<List<EvidencePackageResponse>> list(@PathVariable Long conversationId) {
        return ApiResponse.ok(evidencePackageService.list(conversationId));
    }

    @GetMapping("/api/evidence-packages/{evidencePackageId}/pdf")
    @Operation(summary = "PDF 파일 다운로드", description = "생성된 증거 패키지 PDF 파일을 다운로드합니다. 응답 Content-Type은 application/pdf입니다.")
    public ResponseEntity<FileSystemResource> pdf(@PathVariable Long evidencePackageId) {
        EvidencePackage evidencePackage = evidencePackageService.getEntity(evidencePackageId);
        FileSystemResource resource = evidencePackageService.pdf(evidencePackageId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + evidencePackage.getFileName() + "\"")
                .body(resource);
    }
}
