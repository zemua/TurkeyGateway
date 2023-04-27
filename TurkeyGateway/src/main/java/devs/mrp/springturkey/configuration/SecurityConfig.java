package devs.mrp.springturkey.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Value("${jwk.endpoint}")
	private String jwkEndpoint;

	@Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
	private String issuerUri;

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http
		.authorizeExchange()
		.pathMatchers("/keycloak/token").permitAll()
		.anyExchange().authenticated()
		.and().csrf().disable();

		http.oauth2ResourceServer().jwt();

		return http.build();
	}

}
