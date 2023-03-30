package devs.mrp.springturkey.configuration;

import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

	private RouteLocatorBuilder routeLocatorBuilder;
	private MicroserviceConfiguration microserviceConfiguration;
	private TokenRelayGatewayFilterFactory filterFactory; // for OAuth2

	public RouteConfiguration(RouteLocatorBuilder builder, MicroserviceConfiguration config, TokenRelayGatewayFilterFactory filterFactory) {
		this.routeLocatorBuilder = builder;
		this.microserviceConfiguration = config;
		this.filterFactory = filterFactory;
	}

	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r.path("/something")
						.filters(f -> f.filters(filterFactory.apply())
								.removeRequestHeader("Cookie"))
						.uri(microserviceConfiguration.getTurkeyMainService()))

				.route(r -> r.alwaysTrue()
						.filters(f -> f.filters(filterFactory.apply())
								.removeRequestHeader("Cookie"))
						.uri(microserviceConfiguration.getTurkeyMainService()))

				.build();
	}

}
