package br.com.oficina.orcamento.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")       // todos os endpoints
                .allowedOrigins("*")     // em dev, libera todas as origens
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS");
    }
}
