package devs.mrp.springturkey.mappers.impl;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import devs.mrp.springturkey.mappers.EndpointMapper;

@Component("usermgm")
public class UserManagementBaseEndpointMapper implements EndpointMapper {

	@Value("${turkey.scheme}")
	private String scheme;

	@Value("${turkey.host}")
	private String host;

	@Value("${turkey.usermgm.port}")
	private String port;

	@Override
	public URI getEndpointUri() {
		return URI.create(scheme + "://" + host + ":" + port);
	}

}
