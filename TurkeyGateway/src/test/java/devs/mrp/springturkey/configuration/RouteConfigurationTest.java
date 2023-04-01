package devs.mrp.springturkey.configuration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
properties = {"authService=http://localhost:${wiremock.server.port}",
"couchService=http://localhost:${wiremock.server.port}"})
@Import(RouteConfiguration.class)
@AutoConfigureWireMock(port = 0)
class RouteConfigurationTest {

	@LocalServerPort
	private int port;

	private WebTestClient webTestClient;

	@Autowired
	public RouteConfigurationTest(WebTestClient webTestClient) {
		this.webTestClient = webTestClient;
	}

	@Test
	@WithMockUser
	void testMockRoutes() {
		// A test may only make sense using Spring Contract, still it will not test if the re-reouting is done correctly
		stubFor(get(urlEqualTo("/some/path"))
				.willReturn(aResponse().withBody("path to something")));

		webTestClient
		.get().uri("/some/path")
		.exchange()
		.expectStatus().isOk()
		.expectBody()
		.consumeWith(response -> {
			Assertions.assertThat(response.getResponseBody()).asString()
			.isEqualTo("path to something");
		});
	}

}
