
package com.usuario.quero_ler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	@Bean
	OpenAPI customOpenApi() {
		return new OpenAPI()
				.info(new Info().title("Api Quero Ler")
						.version("1.0")
						.description("Api baseada na pagina Skoob clube da leitura"))
				.components(new Components()
						.addSecuritySchemes("cookieAuth",
								new SecurityScheme()
										.type(SecurityScheme.Type.APIKEY)
										.in(SecurityScheme.In.COOKIE)
										.name("jwt")))
				.addSecurityItem(new SecurityRequirement().addList("cookieAuth"));
	}
}
