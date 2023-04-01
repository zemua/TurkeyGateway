package devs.mrp.springturkey.configuration;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Autowired private ReactiveClientRegistrationRepository clientRegistrationRepository;

	@Value("${turkey.logouturl}")
	private String logouturl;

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

		http
		.csrf().disable()
		.authorizeExchange()
		.anyExchange().permitAll()
		.and()
		.oauth2Login()
		.and()
		.logout(logout -> logout.logoutSuccessHandler(oidcLogoutSuccessHandler()));

		return http.build();
	}

	private ServerLogoutSuccessHandler oidcLogoutSuccessHandler() {
		OidcClientInitiatedServerLogoutSuccessHandler oidcLogoutSuccessHandler =
				new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);

		oidcLogoutSuccessHandler.setLogoutSuccessUrl(URI.create(logouturl));

		return oidcLogoutSuccessHandler;
	}

}
