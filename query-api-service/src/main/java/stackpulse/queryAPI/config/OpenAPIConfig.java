package stackpulse.queryAPI.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig
{
    @Value("${app.server-url:http://localhost:8080}")
    private String serverUrl;
    @Bean
    public OpenAPI stackPulseOpenAPI()
    {
        return new OpenAPI()
                .info(new Info()
                .title("StackPulse Query API")
                .description("REST API for querying technology keyword trends from job postings")
                .version("1.0.0"))
                .servers(List.of(new Server().url(serverUrl)));
    }
}
