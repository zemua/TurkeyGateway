package devs.mrp.springturkey.mappers.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = { KeycloakTokenEndpointMapper.class })
class KeycloakTokenEndpointMapperTest {

	@Autowired
	private KeycloakTokenEndpointMapper mapper;

	@Test
	void test() {
		URI result = mapper.getEndpointUri();
		assertEquals("http://localhost:28080", result.toString());
	}

}
