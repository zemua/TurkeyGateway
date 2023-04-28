package devs.mrp.springturkey.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import devs.mrp.springturkey.mappers.EndpointMapper;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Value("${turkey.paths.token}")
	private String tokenPath;

	@Autowired
	private TokenRelayGatewayFilterFactory relayFilterFactory;

	@Autowired
	@Qualifier("usermgm")
	private EndpointMapper userManagementEndpointMapper;

	@Autowired
	@Qualifier("token")
	private EndpointMapper tokenEndpointMapper;

	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http
		.authorizeExchange()
		.pathMatchers(tokenPath).permitAll()
		.anyExchange().authenticated()
		.and().csrf().disable();

		http.oauth2ResourceServer().jwt();

		return http.build();
	}

	@Bean
	RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r.path("/client/**")
						.filters(f -> f.filters(relayFilterFactory.apply())
								.removeRequestHeader("Cookie"))
						.uri(userManagementEndpointMapper.getEndpointUri().toString()))
				.route(r -> r.path(tokenPath)
						.filters(f -> f.filters(relayFilterFactory.apply())
								.removeRequestHeader("Cookie"))
						.uri(tokenEndpointMapper.getEndpointUri().toString()))
				.build();
	}

}
