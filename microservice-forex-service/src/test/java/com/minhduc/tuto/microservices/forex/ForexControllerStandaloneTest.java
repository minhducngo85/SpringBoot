package com.minhduc.tuto.microservices.forex;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.math.BigDecimal;

import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Mockito will consider all fields having @Mock or @Spy annotation as potential
 * candidates to be injected into the instance annotated with @InjectMocks
 * annotation.
 * 
 * @author Minh Duc Ngo
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ForexControllerStandaloneTest {

    /** Stub object */
    @Mock
    private ExchangeValueRepository repository;

    /** Stub object */
    @Mock
    private Environment environment;

    /** Object to be tested */
    @InjectMocks
    ForexController forexControllerMock;

    /** MVC mock */
    private MockMvc mockMvc;

    @Before
    public void setup() {
	MockitoAnnotations.initMocks(this);
	// MockMvc standalone approach
	mockMvc = MockMvcBuilders.standaloneSetup(forexControllerMock).build();
    }

    /**
     * unit test for
     * {@link ForexController#retrieveExchangeValue(String, String)}
     * 
     * @throws Exception
     */
    @Test
    public void retrieveExchangeValueTestWithEmpty() throws Exception {
	// when
	given(forexControllerMock.retrieveExchangeValue("EUR", "VND")).willReturn(null);
	MockHttpServletResponse response = mockMvc
	        .perform(get("/currency-exchange/from/{from}/to/{to}", "EUR", "VND").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

	// verify
	assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	assertThat(response.getContentAsString()).isEmpty();
    }

    /**
     * unit test for
     * {@link ForexController#retrieveExchangeValue(String, String)}
     * 
     * @throws Exception
     */
    @Test
    public void retrieveExchangeValueTestWithObject() throws Exception {
	// object to be returned
	ExchangeValue exchangeValue = new ExchangeValue(1L, "EUR", "VND", new BigDecimal(27.00));
	// when
	when(environment.getProperty("local.server.port")).thenReturn("80");
	when(forexControllerMock.retrieveExchangeValue("EUR", "VND")).thenReturn(exchangeValue);
	// action
	// MockMVC instance to perform all kind of fake requests (GET, POST,
	// etc.) and we receive a MockHttpServletResponse in return. Keep in
	// mind that’s not a real response either, everything is being
	// simulated.
	MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/currency-exchange/from/EUR/to/VND").accept(MediaType.APPLICATION_JSON);
	MockHttpServletResponse response = mockMvc.perform(request).andReturn()
	        .getResponse();

	// verify
	ObjectMapper mapper = new ObjectMapper();
	assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	assertThat(response.getContentAsString()).isEqualTo(mapper.writeValueAsString(exchangeValue));
    }
}
