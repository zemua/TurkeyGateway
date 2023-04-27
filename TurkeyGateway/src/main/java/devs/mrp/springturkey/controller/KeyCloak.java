package devs.mrp.springturkey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.webflux.ProxyExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devs.mrp.springturkey.mappers.EndpointMapper;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/keycloak")
public class KeyCloak {

	@Autowired
	private EndpointMapper keycloakTokenEndpointMapper;

	@PostMapping("/token")
	public Mono<ResponseEntity<byte[]>> proxy(ProxyExchange<byte[]> proxy) throws Exception {
		return proxy.uri(keycloakTokenEndpointMapper.getEndpointUri().toString()).post();
	}

}
