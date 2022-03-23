package com.minhduc.tuto.springboot.primefaces;

import java.util.Arrays;
import java.util.HashMap;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;

@Configuration
public class JSFConfig implements ServletContextAware {

    /**
     * Registers the JSF Servlet to run when Spring Boot turns on. JSF setup.
     * This allows mapping xhtml requests to our FacesServlet.
     *
     * @return a configured ServletRegistrationBean
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public ServletRegistrationBean facesServletRegistration() {
	// registration
	ServletRegistrationBean registration = new ServletRegistrationBean();
	registration.setServlet(new FacesServlet());
	registration.setName("FacesServlet");
	registration.setUrlMappings(Arrays.asList("*.jsf", "*.xhtml"));
	registration.setLoadOnStartup(1);
	return registration;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.web.context.ServletContextAware#setServletContext(
     * javax.servlet.ServletContext)
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
	// spring boot only works if this is set
	servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
	// servletContext.setInitParameter("primefaces.THEME", "luna-green");
	servletContext.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", "true");
	servletContext.setInitParameter("primefaces.FONT_AWESOME", "true");
	servletContext.setInitParameter("javax.faces.STATE_SAVING_METHOD", "client");
	servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "development");

    }

    /**
     * 
     * @return
     */
    @Bean
    public static ViewScope viewScope() {
	return new ViewScope();
    }

    /**
     * Allows the use of @Scope("view") on Spring @Component, @Service
     * and @Controller beans
     */
    @Bean
    public static CustomScopeConfigurer scopeConfigurer() {
	CustomScopeConfigurer configurer = new CustomScopeConfigurer();
	HashMap<String, Object> hashMap = new HashMap<String, Object>();
	hashMap.put("view", viewScope());
	configurer.setScopes(hashMap);
	return configurer;
    }
}
