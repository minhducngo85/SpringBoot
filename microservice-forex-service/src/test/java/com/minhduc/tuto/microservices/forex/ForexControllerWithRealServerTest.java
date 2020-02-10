package com.minhduc.tuto.microservices.forex;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * When you use @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) or
 * 
 * with @SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT) you’re
 * testing with a real HTTP server so, in this case, you need to use a
 * RestTemplate or TestRestTemplate. The difference between using a random port
 * or a defined port is just that in the first case the default port 8080 (or
 * the one you override with the server.port property) won’t be used but
 * replaced with a randomly-assigned port number. This is helpful when you want
 * to run parallel tests, to avoid port clashing.
 * 
 * 
 * Now the repository is injected in the Spring’s context using @MockBean
 * 
 * A mock (no matter if we talk about ordinary objects or beans) is simply an
 * "empty shell".
 * 
 * 
 * Whereas a spy wraps around an existing object of your class under test.
 * Meaning: when you create a spy, you can decide if method calls going to the
 * spy should be "intercepted" (then you are using the spy as if it would be a
 * mock);
 * 
 * @author Minh Duc Ngo
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "server.port=8080")
@TestPropertySource(locations = "classpath:application.properties")
public class ForexControllerWithRealServerTest {
    @LocalServerPort
    int port;

    /** Stub object */
    @MockBean
    private ExchangeValueRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {

    }

    @Ignore
    @Test
    public void contextLoads() {
    }

    /**
     * unit test for
     * {@link ForexController#retrieveExchangeValue(String, String)}
     * 
     * @throws Exception
     */
    @Test
    public void retrieveExchangeValueTestWithEmpty() throws Exception {
	// given
	given(repository.findByFromAndTo("EUR", "VND")).willReturn(null);
	// when
	ResponseEntity<ExchangeValue> response = restTemplate.getForEntity("/currency-exchange/from/EUR/to/VND", ExchangeValue.class);
	// verify
	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	assertThat(response.getBody()).isNull();
    }

    /**
     * unit test for
     * {@link ForexController#retrieveExchangeValue(String, String)}
     * 
     * @throws Exception
     */
    @Test
    public void retrieveExchangeValueTestWithObject() throws Exception {
	/** given */
	ExchangeValue exchangeValue = new ExchangeValue(1L, "EUR", "VND", new BigDecimal(27.00));
	given(repository.findByFromAndTo("EUR", "VND")).willReturn(exchangeValue);

	/** when */
	ResponseEntity<ExchangeValue> response = restTemplate.getForEntity("/currency-exchange/from/EUR/to/VND", ExchangeValue.class);

	/** verify */
	ObjectMapper mapper = new ObjectMapper();
	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	// verify if the port is set
	assertThat(response.getBody().getPort()).isEqualTo(port);
	assertThat(mapper.writeValueAsString(response.getBody())).isEqualTo(mapper.writeValueAsString(exchangeValue));
    }
}
