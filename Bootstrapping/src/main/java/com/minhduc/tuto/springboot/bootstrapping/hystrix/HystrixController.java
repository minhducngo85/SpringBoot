package com.minhduc.tuto.springboot.bootstrapping.hystrix;

import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * Hystrix isolates the points of access between the services, stops cascading
 * failures across them and provides the fallback options.
 * 
 * @author Minh Duc Ngo
 *
 */
@EnableHystrix
@RestController
public class HystrixController {

    @RequestMapping(value = "/hytrix/hello")
    @HystrixCommand(fallbackMethod = "fallbackHello", commandProperties = {
            @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, value = "5000") })
    public String hello() throws InterruptedException {
	Thread.sleep(6000);
	return "Welcome Hystrix";
    }

    /**
     * the fallback method of "/hytrix/hello"
     * 
     * @return
     */
    String fallbackHello() {
	return "Request fails. It takes long time to response";
    }
}
