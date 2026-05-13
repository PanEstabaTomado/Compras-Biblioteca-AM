package dfy1103.magaq.compra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${empleado.url}")
    private String empleadoUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(empleadoUrl)   // http://localhost:8083
                .build();
    }
}