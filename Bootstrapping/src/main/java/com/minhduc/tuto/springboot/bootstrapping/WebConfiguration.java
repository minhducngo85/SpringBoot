package com.minhduc.tuto.springboot.bootstrapping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.common.base.Predicates;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * Web configuration with Cors support
 *
 */
@Configuration
@EnableSwagger2 // to eanble swagger 2
@EnableWebMvc // to enable cors support
public class WebConfiguration implements WebMvcConfigurer {

    // enable cors support
    @Override
    public void addCorsMappings(CorsRegistry registry) {
	registry.addMapping("/**");
    }

    // new code commet
    @Bean
    public Docket api() {
	// @formatter:off
	return new Docket(DocumentationType.SWAGGER_2).select()
	        // .apis(RequestHandlerSelectors.any())
	        .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
	        // .paths(PathSelectors.any())
	        // .paths(PathSelectors.ant("/swagger2-demo"))
	        .build();
	// @formatter:on
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
	registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
