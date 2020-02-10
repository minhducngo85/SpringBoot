package com.minhduc.tuto.springboot.bootstrapping.servletfilter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A filter is an object used to intercept the HTTP requests and responses of
 * your application. By using filter, we can perform two operations at two
 * instances −
 * 
 * Before sending the request to the controller Before sending a response to the
 * client.
 * 
 * @author UE1PHOT
 *
 */
@Component
public class AppSimpleFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppSimpleFilter.class.getName());

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain) throws IOException, ServletException {
	LOGGER.info("Remote Host:" + request.getRemoteHost());
	LOGGER.debug("Remote Address:" + request.getRemoteAddr());
	filterchain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterconfig) throws ServletException {
    }
}
