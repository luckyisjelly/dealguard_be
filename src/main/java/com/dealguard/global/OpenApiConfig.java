package com.dealguard.global;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DealGuard API")
                        .version("0.1.0")
                        .description("중고거래 게시글과 채팅 메시지를 분석해 거래 조건을 추출하고, 분쟁 예방 alert와 PDF 증거 패키지를 생성하는 백엔드 API입니다."))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("회원가입 또는 로그인 응답의 accessToken 값을 입력하세요. Swagger에는 Bearer 없이 토큰 문자열만 입력하면 됩니다.")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    @Bean
    OpenApiCustomizer commonErrorResponsesCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                    ApiResponses responses = operation.getResponses();
                    responses.addApiResponse("400", errorResponse("잘못된 요청입니다. 요청 값, JSON 형식, 필수 값 누락 여부를 확인해 주세요."));
                    responses.addApiResponse("401", errorResponse("인증이 필요하거나 토큰이 유효하지 않습니다. access token 또는 refresh token을 확인해 주세요."));
                    responses.addApiResponse("403", errorResponse("요청한 API에 접근할 권한이 없습니다."));
                    responses.addApiResponse("404", errorResponse("요청한 리소스를 찾을 수 없습니다."));
                    responses.addApiResponse("409", errorResponse("데이터 제약 조건을 위반했습니다. 중복 값이나 참조 관계를 확인해 주세요."));
                    responses.addApiResponse("415", errorResponse("지원하지 않는 Content-Type입니다. application/json을 사용해 주세요."));
                    responses.addApiResponse("500", errorResponse("서버 내부 오류입니다. 로그 확인이 필요합니다."));
                }));
    }

    private io.swagger.v3.oas.models.responses.ApiResponse errorResponse(String description) {
        return new io.swagger.v3.oas.models.responses.ApiResponse()
                .description(description)
                .content(new Content().addMediaType(
                        org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().example(ApiResponse.error(description))));
    }
}
