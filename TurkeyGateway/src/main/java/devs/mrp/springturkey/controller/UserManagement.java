package devs.mrp.springturkey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.webflux.ProxyExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import devs.mrp.springturkey.mappers.EndpointMapper;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/client")
public class UserManagement {

	@Autowired
	@Qualifier("usermgm")
	private EndpointMapper userManagementEndpointMapper;

	@Value("${turkey.paths.createuser}")
	private String createPath;
	@Value("${turkey.paths.verifyemail}")
	private String verifyPath;
	@Value("${turkey.paths.changepass}")
	private String passwordPath;

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('SCOPE_create_user')")
	public Mono<ResponseEntity<byte[]>> proxyCreate(ProxyExchange<byte[]> proxy) throws Exception {
		return proxy.uri(userManagementEndpointMapper.getEndpointUri().toString() + createPath).post();
	}

	@PutMapping("/verify")
	@PreAuthorize("hasAuthority('SCOPE_send_verify')")
	public Mono<ResponseEntity<byte[]>> proxyVerify(ProxyExchange<byte[]> proxy) throws Exception {
		return proxy.uri(userManagementEndpointMapper.getEndpointUri().toString() + verifyPath).post();
	}

	@PutMapping("/password")
	@PreAuthorize("hasAuthority('SCOPE_send_update_password')")
	public Mono<ResponseEntity<byte[]>> proxyPassword(ProxyExchange<byte[]> proxy) throws Exception {
		return proxy.uri(userManagementEndpointMapper.getEndpointUri().toString() + passwordPath).post();
	}

}
