package com.minhduc.tuto.springboot.hibernate;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

/**
 * 
 * Reference link: https://o7planning.org/de/11665/anleitung-spring-boot-hibernate-und-spring-transaction
 * 
 * Achtung: Spring Boot wird nach dem Standard JPA automatisch konfigurieren und
 * die Spring BEAN beziehend mit JPA erstellen. Die automatischen Konfiguration
 * vom Spring Boot fasst um:
 * 
 * DataSourceAutoConfiguration DataSourceTransactionManagerAutoConfiguration
 * HibernateJpaAutoConfiguration
 * 
 * Das Zweck in der Applikation ist die Verwendung von Hibernate, Deshalb sollen
 * Sie die obengemeinten automatischen Konfiguration vom Spring Boot
 * ​​​​​​​deaktivieren.
 * 
 * @author Minh Duc Ngo
 *
 */
@Configuration
@EnableAutoConfiguration(exclude = { //
        DataSourceAutoConfiguration.class, //
        DataSourceTransactionManagerAutoConfiguration.class, //
        HibernateJpaAutoConfiguration.class })
public class HibernateConfiguration {
    
    @Autowired
    private Environment env;

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
	DriverManagerDataSource dataSource = new DriverManagerDataSource();
	// See: application.properties
	dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
	dataSource.setUrl(env.getProperty("spring.datasource.url"));
	dataSource.setUsername(env.getProperty("spring.datasource.username"));
	dataSource.setPassword(env.getProperty("spring.datasource.password"));
	return dataSource;
    }

    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) throws Exception {
	Properties properties = new Properties();
	properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
	properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
	properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
	properties.put("current_session_context_class", env.getProperty("spring.jpa.properties.hibernate.current_session_context_class"));
	LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
	// Package contain entity classes
	factoryBean.setPackagesToScan(new String[] { "com.minhduc.tuto.springboot.hibernate.entity" });
	factoryBean.setDataSource(dataSource);
	factoryBean.setHibernateProperties(properties);
	factoryBean.afterPropertiesSet();
	//
	SessionFactory sf = factoryBean.getObject();
	return sf;
    }

    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
	HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
	return transactionManager;
    }
}
