package kr.hhplus.be.server.config.swagger;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("콘서트 티켓팅 API Swagger")
                .description("대기열의 개념을 고려한 콘서트 티켓팅 서비스의 API 입니다.")
                .version("1.0.0");
    }
}