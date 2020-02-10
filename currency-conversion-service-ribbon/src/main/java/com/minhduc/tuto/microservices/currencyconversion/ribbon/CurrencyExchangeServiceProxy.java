package com.minhduc.tuto.microservices.currencyconversion.ribbon;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Freign proxy client
 * 
 * @author Minh Duc Ngo
 *
 *         
 */
//Limitation: the url is hard coded. You ar only able to communicate with only on server.
// -> using ribbon client
//@FeignClient(name = "forex-service", url = "localhost:8001")
@FeignClient(name="forex-service")
@RibbonClient(name="forex-service") // we configured ribbon to distribute load to instances. 
public interface CurrencyExchangeServiceProxy {
    /**
     * @GetMapping("/currency-exchange/from/{from /to/{to}"): - URI of the
     *                                            service we would want to
     *                                            consume
     * 
     * @param from
     * @param to
     * @return
     */
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyConversionBean retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);
}
