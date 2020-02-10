package com.minhduc.tuto.microservices.forex;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Minh Duc Ngo
 *
 */
@RunWith(SpringRunner.class)
// initialize spring context
@WebMvcTest(ForexController.class)
// gets instance of Object to be tested, our MockMVC instance gets
// autoconfigured and available in the context (so we can autowire it as you see
// below in the code).
@TestPropertySource(locations = "classpath:application.properties", properties = { "local.server.port=8000" })
public class ForexControllerWithApplicationContextTest {
    @Autowired
    private MockMvc mvc;

    /** Stub object */
    @MockBean
    private ExchangeValueRepository repository;

    @Before
    public void setup() {

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
	MockHttpServletResponse response = mvc
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
	// given
	ExchangeValue exchangeValue = new ExchangeValue(1L, "EUR", "VND", new BigDecimal(27.00));
	given(repository.findByFromAndTo("EUR", "VND")).willReturn(exchangeValue);

	// when
	MockHttpServletResponse response = mvc.perform(get("/currency-exchange/from/EUR/to/VND").accept(MediaType.APPLICATION_JSON)).andReturn()
	        .getResponse();

	// verify
	ObjectMapper mapper = new ObjectMapper();
	assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	assertThat(response.getContentAsString()).isEqualTo(mapper.writeValueAsString(exchangeValue));
    }
}
