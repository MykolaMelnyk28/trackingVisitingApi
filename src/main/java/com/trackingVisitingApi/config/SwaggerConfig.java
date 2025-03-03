package com.trackingVisitingApi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Tracking Visiting API",
                version = "1.0",
                contact = @Contact(
                        name = "MykolaMelnyk28",
                        email = "mykolamelnyk338@gmail.com"
                )
        ),
        servers = {
                @Server(
                        description = "LOCAL ENV",
                        url = "http://localhost:8080/api"
                )
        }
)
public class SwaggerConfig { }
