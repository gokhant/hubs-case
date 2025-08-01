package hubs.digital.wallet.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "HUBS Digital Wallet API",
                version = "1.0",
                description = "API for digital wallet operations"
        )
)
public class SwaggerConfig {
}