package com.minhduc.tuto.oauth2service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

public class RestTemplateUtils {

    /**
     * Logger object
     */
    private static final Logger LOGGER = LogManager.getLogger(RestTemplateUtils.class);

    /**
     * 
     * @return a default proxy rest template with all settings from
     *         proxy.properties
     */
    public static RestTemplate initRestTemplate() {
	return initTemplate(null, 0, null, null);
    }

    /**
     * Method to init the standard REST template.
     * 
     * @return The rest template object.
     */
    private static RestTemplate initTemplate(String proxyUrl, int proxyPort, String proxyUser, String proxyPassword) {
	HttpClientBuilder clientBuilder = HttpClientBuilder.create();
	if (!StringUtils.isEmpty(proxyUrl)) { // not null and empty check
	    HttpHost myProxy = new HttpHost(proxyUrl, proxyPort);
	    clientBuilder.setProxy(myProxy);
	    if (!StringUtils.isEmpty(proxyUser) && !StringUtils.isEmpty(proxyPassword)) {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(proxyUrl, proxyPort), new UsernamePasswordCredentials(proxyUser, proxyPassword));
		clientBuilder.setDefaultCredentialsProvider(credsProvider);
	    }
	    clientBuilder.disableCookieManagement();
	}

	HttpClient httpClient = clientBuilder.build();
	HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
	factory.setHttpClient(httpClient);

	return new RestTemplate(factory);
    }

    @SuppressWarnings("rawtypes")
    public static String[] refreshToken(String refreshToken, String clientId, String accessTokenUrl) {
	HttpHeaders mheaders = new HttpHeaders();
	mheaders.add("Content-Type", "application/x-www-form-urlencoded");
	String body = "grant_type=refresh_token";
	body += "&client_id=%s";
	body += "&refresh_token=%s";
	;
	HttpEntity<Object> request = new HttpEntity<Object>(String.format(body, clientId, refreshToken), mheaders);
	ResponseEntity<LinkedHashMap> response = null;
	RestTemplate restTemplate = RestTemplateUtils.initRestTemplate();
	try {
	    response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, LinkedHashMap.class);
	    LOGGER.debug("Refresh Token Response ---------" + response.getBody());
	    return new String[] { (String) response.getBody().get("access_token"), (String) response.getBody().get("refresh_token"),
	            (String) response.getBody().get("session_state"), new Gson().toJson(response.getBody()) };
	} catch (HttpClientErrorException e) {
	    LOGGER.error(e.getMessage());
	    return new String[] { "", "", "" };
	}
    }

    @SuppressWarnings("rawtypes")
    public static String[] requestTokenFromAuthCode(String code, String sessionstate, String clientId, String clientSecret, String callbackUrl,
            String accessTokenUrl) {
	LOGGER.debug("Code: {}", code);
	HttpHeaders mheaders = new HttpHeaders();
	mheaders.add("Content-Type", "application/x-www-form-urlencoded");
	String body = "grant_type=authorization_code";
	body += "&code=%s";
	body += "&client_id=%s";
	body += "&session_state=%s";
	body += "&redirect_uri=" + callbackUrl;
	if (!StringUtils.isEmpty(clientSecret)) {
	    body += "&client_secret=" + clientSecret;
	}
	body = String.format(body, code, clientId, sessionstate);
	HttpEntity<Object> request = new HttpEntity<Object>(body, mheaders);
	LOGGER.debug("Requets Body: {}", body);
	ResponseEntity<Map> response = null;
	RestTemplate restTemplate = RestTemplateUtils.initRestTemplate();
	try {
	    response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, Map.class);
	    LOGGER.debug("Access Token Response ---------" + response.getBody());
	    return new String[] { (String) response.getBody().get("access_token"), (String) response.getBody().get("refresh_token"),
	            (String) response.getBody().get("session_state"), new Gson().toJson(response.getBody()) };
	} catch (HttpClientErrorException e) {
	    LOGGER.error(e.getMessage());
	    return new String[] { "", "", "" };
	}
    }

}
