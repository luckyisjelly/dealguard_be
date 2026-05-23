package com.dealguard.global;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
                        .description("중고거래 게시글과 채팅 메시지를 분석해 거래 조건을 추출하고, 분쟁 예방 alert와 PDF 증거 패키지를 생성하는 백엔드 API입니다."));
    }
}
