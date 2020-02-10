package com.minhduc.tuto.microservices.forex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForexController {

    private static Logger logger = LoggerFactory.getLogger(ForexController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private ExchangeValueRepository repository;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
	logger.debug("GET /currency-exchange/from/" + from + "/to/" + to);
	ExchangeValue exchangeValue = repository.findByFromAndTo(from, to);
	if (exchangeValue != null) {
	    String port = environment.getProperty("local.server.port");
	    logger.info("local.server.port : {}", port);
	    exchangeValue.setPort(Integer.parseInt(port));
	    return exchangeValue;
	} else {
	    return null;
	}

    }
}
