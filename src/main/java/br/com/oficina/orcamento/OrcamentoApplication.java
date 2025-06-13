package br.com.oficina.orcamento;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(title = "API Oficina", version = "v1"),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local (dev)"),
                @Server(url = "https://0b54-2804-6a84-1053-6e00-9575-221a-f25-e849.ngrok-free.app", description = "Tunnel Ngrok")
        }
)
public class OrcamentoApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrcamentoApplication.class, args);
    }
}
