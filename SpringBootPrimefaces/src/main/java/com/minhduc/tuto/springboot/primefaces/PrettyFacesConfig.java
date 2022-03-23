package com.minhduc.tuto.springboot.primefaces;

import javax.servlet.DispatcherType;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ocpsoft.pretty.PrettyFilter;

@Configuration
public class PrettyFacesConfig {
    /**
     * Configures PrettyFaces by registering a required Filter.
     *
     * @return A configured PrettyFaces filter.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public FilterRegistrationBean prettyFilter() {
        FilterRegistrationBean rewriteFilter = new FilterRegistrationBean(new PrettyFilter());
        // Filter Forward, Request, Asyc, and Error-related requests.
        rewriteFilter.setDispatcherTypes(DispatcherType.FORWARD, DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR);
        // Attach the filter to the root URL for the website. e.g.) http://www.example.com/*
        rewriteFilter.addUrlPatterns("/*");
        return rewriteFilter;
    }
}
