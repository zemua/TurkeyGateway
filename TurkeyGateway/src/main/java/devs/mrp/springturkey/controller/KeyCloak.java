package devs.mrp.springturkey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.webflux.ProxyExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import devs.mrp.springturkey.mappers.EndpointMapper;
import reactor.core.publisher.Mono;

@RestController
public class KeyCloak {

	@Autowired
	@Qualifier("token")
	private EndpointMapper keycloakTokenEndpointMapper;

	@Value("${turkey.paths.token}")
	private String tokenPath;
	@Value("${turkey.paths.realm}")
	private String realmPath;

	@PostMapping("/keycloak/token")
	public Mono<ResponseEntity<byte[]>> proxyToken(ProxyExchange<byte[]> proxy) throws Exception {
		return proxy.uri(keycloakTokenEndpointMapper.getEndpointUri().toString() + tokenPath).post();
	}

}
