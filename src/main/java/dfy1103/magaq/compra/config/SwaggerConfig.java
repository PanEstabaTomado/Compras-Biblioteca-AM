package dfy1103.magaq.compra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;
@Configuration
public class SwaggerConfig {

    @Value("${springdoc.servers[0].url:http://localhost:8098}")
    private String serverUrl;

    @Value("${springdoc.servers[0].description:Via del Api Gateway}")
    private String serverDescription;

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");
        return new OpenAPI()
                .info(new Info()
                        .title("API 2026 Sistema y Registro de Compras de la Biblioteca AM")
                        .version("1.0")
                        .description("Sistema y Registro de compras hechos por la Biblioteca AM para el re-stock de libros."))
                .servers(List.of(
                        new Server()
                                .url(serverUrl)
                                .description(serverDescription)
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement);
    }
}
