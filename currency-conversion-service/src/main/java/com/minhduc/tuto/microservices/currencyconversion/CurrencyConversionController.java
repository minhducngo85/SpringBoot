package com.minhduc.tuto.microservices.currencyconversion;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Rest client using restTemplate
 */
@RestController
public class CurrencyConversionController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CurrencyExchangeServiceProxy mProxy;

    /**
     * consumer using rest template (Rest Client using Rest template9
     * 
     * @param from
     * @param to
     * @param quantity
     * @return
     */
    @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
	logger.debug("convert currency");
	Map<String, String> uriVariables = new HashMap<>();
	uriVariables.put("from", from);
	uriVariables.put("to", to);
	ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(
	        "http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class, uriVariables);
	CurrencyConversionBean response = responseEntity.getBody();
	if (response != null) {
	    response.setQuantity(quantity);
	    response.setTotalCalculatedAmount(quantity.multiply(response.getConversionMultiple()));
	}
	return response;
    }

    /**
     * Consumer using Feign client
     * 
     * @param from
     * @param to
     * @param quantity
     * @return
     */
    @GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrencyFreign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
	CurrencyConversionBean response = mProxy.retrieveExchangeValue(from, to);
	logger.info("{}", response);
	if (response != null) {
	    response.setQuantity(quantity);
	    response.setTotalCalculatedAmount(quantity.multiply(response.getConversionMultiple()));
	}
	return response;
    }

}
