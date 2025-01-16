package kr.hhplus.be.server.config.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@SecurityScheme(
        type = SecuritySchemeType.APIKEY,
        name = "USER_ID",
        description = "userID을 입력해주세요.",
        in = SecuritySchemeIn.HEADER)
@SecurityScheme(
        type = SecuritySchemeType.APIKEY,
        name = "WAITLIST_TOKEN",
        description = "대기열 트큰을 입력해주세요.",
        in = SecuritySchemeIn.HEADER)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .security(getSecurityRequirement());
    }

    private Info apiInfo() {
        return new Info()
                .title("콘서트 티켓팅 API Swagger")
                .description("대기열의 개념을 고려한 콘서트 티켓팅 서비스의 API 입니다.")
                .version("1.0.0");
    }

    private List<SecurityRequirement> getSecurityRequirement() {
        List<SecurityRequirement> requirements = new ArrayList<>();
        requirements.add(new SecurityRequirement().addList("USER_ID"));
        requirements.add(new SecurityRequirement().addList("WAITLIST_TOKEN"));
        return requirements;
    }
}