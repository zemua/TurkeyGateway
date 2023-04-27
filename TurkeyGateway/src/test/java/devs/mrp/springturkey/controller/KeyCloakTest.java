package devs.mrp.springturkey.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import devs.mrp.springturkey.mappers.EndpointMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.yml")
class KeyCloakTest {

	@Autowired
	private WebTestClient webTestClient;
	private MockWebServer mockWebServer;
	@MockBean
	@Qualifier("token")
	private EndpointMapper keycloakTokenEndpointMapper;

	@BeforeEach
	void setup() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();
	}

	@AfterEach
	public void tearDown() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	void testTokenForwarding() throws InterruptedException, URISyntaxException {
		mockWebServer.enqueue(new MockResponse()
				.setBody(mockResponseBody())
				.setResponseCode(200));

		when(keycloakTokenEndpointMapper.getEndpointUri()).thenReturn(new URI("http://localhost:" + mockWebServer.getPort() + "/auth/realms/Turkey/protocol/openid-connect/token"));

		MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
		data.add("client_id", "clientWithScope");
		data.add("client_secret", "0o8yTHK4Z5zYO46JNFYvm7f78ig9Dlwu");
		data.add("grant_type", "client_credentials");

		webTestClient.post()
		.uri("/keycloak/token")
		.header("Content-Type", "application/x-www-form-urlencoded")
		.body(BodyInserters.fromFormData(data))
		.exchange()
		.expectStatus().isEqualTo(HttpStatusCode.valueOf(200))
		.expectBody()
		.jsonPath("$.access_token").isEqualTo("sometoken")
		.jsonPath("$.expires_in").isEqualTo(300)
		.jsonPath("$.refresh_expires_in").isEqualTo(0)
		.jsonPath("$.token_type").isEqualTo("Bearer")
		.jsonPath("$.not-before-policy").isEqualTo(0)
		.jsonPath("$.scope").isEqualTo("some scope");

		RecordedRequest request = mockWebServer.takeRequest();
		assertEquals("POST", request.getMethod());
		assertEquals("/auth/realms/Turkey/protocol/openid-connect/token", request.getPath());
		assertEquals("application/x-www-form-urlencoded;charset=UTF-8", request.getHeader("Content-Type"));
		assertEquals("client_id=clientWithScope&client_secret=0o8yTHK4Z5zYO46JNFYvm7f78ig9Dlwu&grant_type=client_credentials", request.getBody().readUtf8());
	}

	private String mockResponseBody() {
		return "{\n"
				+ "    \"access_token\": \"sometoken\",\n"
				+ "    \"expires_in\": 300,\n"
				+ "    \"refresh_expires_in\": 0,\n"
				+ "    \"token_type\": \"Bearer\",\n"
				+ "    \"not-before-policy\": 0,\n"
				+ "    \"scope\": \"some scope\"\n"
				+ "}";
	}

}
