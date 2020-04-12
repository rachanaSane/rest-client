package energy.au.rest.client.consumerest.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestAPIProcessorTest {

	@Autowired
	private WebTestClient webClient;

	@Test
	public void testGetMusicFestivals() {

		this.webClient.get().uri("http://eacodingtest.digital.energyaustralia.com.au/api/v1/festivals").exchange()
				.expectStatus().isOk().expectBody().jsonPath("$[?(@.name == 'Twisted Tour')]").exists();

	}
}
