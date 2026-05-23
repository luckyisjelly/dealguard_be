package com.dealguard.product;

import com.dealguard.global.ApiResponse;
import com.dealguard.global.SecurityUtil;
import com.dealguard.product.dto.ProductPostRequest;
import com.dealguard.product.dto.ProductPostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product-posts")
@Tag(name = "상품 게시글", description = "중고거래 상품 게시글을 생성하고 관리하는 API")
public class ProductPostController {

    private final ProductPostService productPostService;
    private final SecurityUtil securityUtil;

    public ProductPostController(ProductPostService productPostService, SecurityUtil securityUtil) {
        this.productPostService = productPostService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    @Operation(summary = "상품 게시글 생성", description = "거래 분석의 기준이 되는 상품 게시글 정보를 저장합니다. 가격, 상품 상태, 하자 설명, 환불 정책, 거래 위치 등을 입력합니다.")
    public ApiResponse<ProductPostResponse> create(@Valid @RequestBody ProductPostRequest request) {
        return ApiResponse.ok(productPostService.create(securityUtil.currentUser(), request));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "상품 게시글 조회", description = "상품 게시글 ID로 단건 상세 정보를 조회합니다.")
    public ApiResponse<ProductPostResponse> get(@PathVariable Long postId) {
        return ApiResponse.ok(productPostService.get(postId));
    }

    @PatchMapping("/{postId}")
    @Operation(summary = "상품 게시글 수정", description = "상품 게시글의 제목, 가격, 상태 설명, 환불 정책 등 입력된 필드를 수정합니다.")
    public ApiResponse<ProductPostResponse> update(@PathVariable Long postId, @Valid @RequestBody ProductPostRequest request) {
        return ApiResponse.ok(productPostService.update(postId, request));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "상품 게시글 삭제", description = "상품 게시글 ID에 해당하는 게시글을 삭제합니다.")
    public ApiResponse<Void> delete(@PathVariable Long postId) {
        productPostService.delete(postId);
        return ApiResponse.ok();
    }
}
