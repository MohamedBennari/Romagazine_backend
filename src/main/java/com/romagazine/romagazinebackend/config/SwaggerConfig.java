package com.romagazine.romagazinebackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Romagazine Backend API")
                        .description("API documentation for Romagazine Backend")
                        .version("1.0.0"));
    }

    @Bean
    public OperationCustomizer customOperationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            if (operation.getRequestBody() != null) {
                RequestBody requestBody = operation.getRequestBody();
                if (requestBody.getContent() != null && 
                    requestBody.getContent().get("multipart/form-data") != null) {
                    Schema<?> schema = requestBody.getContent().get("multipart/form-data").getSchema();
                    if (schema != null && schema.getProperties() != null) {
                        Schema<?> fileSchema = new Schema<>();
                        fileSchema.setType("string");
                        fileSchema.setFormat("binary");
                        schema.getProperties().put("file", fileSchema);
                    }
                }
            }
            return operation;
        };
    }
} 