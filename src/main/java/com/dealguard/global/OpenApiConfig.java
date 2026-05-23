package com.dealguard.global;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
}
