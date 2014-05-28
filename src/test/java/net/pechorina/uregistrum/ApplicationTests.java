package net.pechorina.uregistrum;

import static org.junit.Assert.*;

import java.util.List;

import net.pechorina.uregistrum.model.Endpoint;
import net.pechorina.uregistrum.service.EndpointService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
@IntegrationTest
public class ApplicationTests {
	static final Logger logger = LoggerFactory
			.getLogger(ApplicationTests.class);

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private Environment env;

	@Value("${security.user.name}")
	private String user;

	@Value("${security.user.password}")
	private String password;

	@Autowired
	private EndpointService endpointService;

	@Test
	public void testCreate() throws Exception {
		Endpoint e = new Endpoint("testsrv1", "localhost",
				"localhost.localdomain", 11401, "/api", "admin", "test");
		assertNotNull(endpointService.saveEndpoint(e));
	}

	@Test
	public void testRetrieve() throws Exception {
		Endpoint e = new Endpoint("testsrv2", "localhost",
				"localhost.localdomain", 11401, "/api", "admin", "test");
		Endpoint savedEntity = endpointService.saveEndpoint(e);
		
		Endpoint e2 = endpointService.lookupEndpoint("testsrv2");
		assertNotNull(e2);
		assertEquals(savedEntity, e2);
	}

	@Test
	public void testRestPost() throws Exception {
		Endpoint e3 = new Endpoint("testsrv3", "localhost",
				"localhost.localdomain", 11401, "/api", "admin", "test");

		RestTemplate restTemplate = new TestRestTemplate(user, password);
		restTemplate
				.postForLocation("http://localhost:11401/api/endpoints", e3);

		ResponseEntity<Endpoint> entity = restTemplate
				.getForEntity("http://localhost:11401/api/endpoints/testsrv3",
						Endpoint.class);
		logger.debug("Received: " + entity);

		assertEquals(e3, entity.getBody());
	}

	@Test
	public void testRestPut() throws Exception {
		Endpoint e4 = new Endpoint("testsrv4", "localhost",
				"localhost.localdomain", 11401, "/api", "admin", "test");

		RestTemplate restTemplate = new TestRestTemplate(user, password);
		restTemplate.put("http://localhost:11401/api/endpoints/testsrv4", e4);

		ResponseEntity<Endpoint> entity = restTemplate
				.getForEntity("http://localhost:11401/api/endpoints/testsrv4",
						Endpoint.class);

		assertEquals(e4, entity.getBody());
	}

	@Test
	public void testRestSave() throws Exception {
		@SuppressWarnings("rawtypes")
		ResponseEntity<List> entity = new TestRestTemplate(user, password)
				.getForEntity("http://localhost:11401/api/endpoints",
						List.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
	}

	@Test
	public void testRestDelete() throws Exception {
		Endpoint e5 = new Endpoint("testsrv5", "localhost",
				"localhost.localdomain", 11401, "/api", "admin", "test");

		RestTemplate restTemplate = new TestRestTemplate(user, password);
		restTemplate
				.postForLocation("http://localhost:11401/api/endpoints", e5);

		restTemplate.delete("http://localhost:11401/api/endpoints/testsrv5");

		ResponseEntity<Endpoint> entity = restTemplate
				.getForEntity("http://localhost:11401/api/endpoints/testsrv5",
						Endpoint.class);
		logger.debug("Received: " + entity);

		assertEquals(entity.getStatusCode(), HttpStatus.NOT_FOUND);
	}
}
