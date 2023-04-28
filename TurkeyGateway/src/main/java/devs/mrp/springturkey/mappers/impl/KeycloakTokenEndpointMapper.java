package devs.mrp.springturkey.mappers.impl;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import devs.mrp.springturkey.mappers.EndpointMapper;

@Component("token")
public class KeycloakTokenEndpointMapper implements EndpointMapper {

	@Value("${turkey.authscheme}")
	private String scheme;

	@Value("${turkey.authhost}")
	private String host;

	@Value("${turkey.authport}")
	private String port;

	@Value("${turkey.realm}")
	private String realm;

	@Override
	public URI getEndpointUri() {
		return URI.create(scheme + "://" + host + ":" + port);
	}

}
