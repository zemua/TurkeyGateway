package devs.mrp.springturkey.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import devs.mrp.springturkey.mappers.EndpointMapper;
import devs.mrp.springturkey.mappers.impl.KeycloakTokenEndpointMapper;
import devs.mrp.springturkey.mappers.impl.UserManagementBaseEndpointMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@EnableAutoConfiguration
@ContextConfiguration(classes = { SecurityConfig.class, UserManagementBaseEndpointMapper.class, KeycloakTokenEndpointMapper.class })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"server.port=28080"})
class SecurityConfigTest {

	@Autowired
	private WebTestClient webTestClient;

	private static MockWebServer mockWebServer;

	@Autowired
	@Qualifier("usermgm")
	private EndpointMapper userManagementEndpointMapper;

	@Autowired
	@Qualifier("token")
	private EndpointMapper tokenEndpointMapper;

	@BeforeAll
	static void setup() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();
	}

	@AfterAll
	static void tearDown() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	void testRoutingUserManagement() throws InterruptedException {
		mockWebServer.enqueue(new MockResponse()
				.setBody("some body")
				.setResponseCode(200));

		String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());

		webTestClient.get()
		.uri("/client/hello")
		.exchange()
		.expectStatus().is2xxSuccessful()
		.expectBody().equals("some body");

		RecordedRequest recordedRequest = mockWebServer.takeRequest();
		assertEquals("GET", recordedRequest.getMethod());
		assertEquals("/client/hello", recordedRequest.getPath());
	}

	@Test
	void testRoutingTokenFetch() {
		fail("Not yet implemented");
	}

	@Test
	void testUserManagementAuth() {
		fail("Not yet implemented");
	}

}
