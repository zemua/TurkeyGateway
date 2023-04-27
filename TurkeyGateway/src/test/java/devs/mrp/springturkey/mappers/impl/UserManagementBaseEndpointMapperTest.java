package devs.mrp.springturkey.mappers.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = { UserManagementBaseEndpointMapper.class })
class UserManagementBaseEndpointMapperTest {

	@Autowired
	private UserManagementBaseEndpointMapper mapper;

	@Test
	void test() {
		URI result = mapper.getEndpointUri();
		assertEquals("http://localhost:8081", result.toString());
	}

}
