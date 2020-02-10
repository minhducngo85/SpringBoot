package com.minhduc.tuto.springboot.bootstrapping.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * You can use the Interceptor in Spring Boot to perform operations under the
 * following situations −
 * 
 * Before sending the request to the controller
 * 
 * Before sending the response to the client
 * 
 * The following are the three methods you should know about while working on
 * Interceptors −
 * 
 * preHandle() method − This is used to perform operations before sending the
 * request to the controller. This method should return true to return the
 * response to the client.
 * 
 * postHandle() method − This is used to perform operations before sending the
 * response to the client.
 * 
 * afterCompletion() method − This is used to perform operations after
 * completing the request and response.
 * 
 * 
 * @author UE1PHOT
 *
 */
@Component
public class ProductServiceInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceInterceptor.class.getName());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	LOGGER.debug("Pre Handle method is Calling");
	return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	LOGGER.debug("Post Handle method is Calling");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
	LOGGER.debug("Request and Response is completed");
    }
}
