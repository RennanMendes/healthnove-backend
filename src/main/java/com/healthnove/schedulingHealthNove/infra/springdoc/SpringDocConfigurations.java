package com.healthnove.schedulingHealthNove.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        ExternalDocumentation externalDocumentation = new ExternalDocumentation()
                .description("Documentação Externa da API no GitHub")
                .url("https://github.com/projetosUninove/healthNove-backend");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("HealthNove API")
                        .description(" A API de Agendamento de Consultas para a clínica HealthNove é um sistema de" +
                                " gerenciamento de consultas médicas que segue as normas e requisitos da clínica." +
                                " Este sistema permite que pacientes agendem consultas com médicos de diferentes " +
                                "especialidades de forma eficiente, segura e conveniente.")
                        .version("0.0.1")
                        .contact(new Contact()
                                .name("por email")
                                .email("amrennan@gmail.com"))
                        .license(new License()
                                .name("Licença MIT")
                                .url("https://www.mit.edu/~amini/LICENSE.md")))
                .externalDocs(externalDocumentation);
    }

}