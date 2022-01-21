package com.minhduc.tuto.oauth2service.https;

import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

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

import com.github.scribejava.core.utils.OAuthEncoder;
import com.google.gson.Gson;
import com.minhduc.tuto.oauth2service.AppAndOsUtils;
import com.minhduc.tuto.oauth2service.SocketHandler;

public class HttpsOauthService {

    /**
     * Logger object
     */
    private static final Logger LOGGER = LogManager.getLogger(HttpsOauthService.class);

    /** Authorize and token endpoint */
    private static final String AUTHORIZE_URL = "http://localhost:8080/auth/realms/SpringBootRealm/protocol/openid-connect/auth?response_type=code&client_id=%s&redirect_uri=%s";
    private static final String ACCESS_TOKEN_URL = "http://localhost:8080/auth/realms/SpringBootRealm/protocol/openid-connect/token";

    /** Client information */
    private static final String ClientId = "login-app";
    public static final String ClientSecret = "client_secret";
    private static int port = 9200;
    private static final String CALLBACK_URL = "https://localhost:" + port + "/auth_callback";

    public static String getAuthorizationUrl() {
	return String.format(AUTHORIZE_URL, ClientId, OAuthEncoder.encode(CALLBACK_URL));
    }

    /**
     * to start ssl web socket server
     * 
     * @param args
     */
    public static void main(String[] args) {
	LOGGER.debug("Please meake that the KeyCloak server started!");
	// httpd(port);
	new HttpsOauthService().startServer();
	browse(getAuthorizationUrl());
    }

    public void startServer() {
	Runnable serverTask = new Runnable() {
	    @Override
	    public void run() {
		try {
		    // ServerSocket serverSocket = new ServerSocket(port);
		    KeyStore ks = KeyStore.getInstance("PKCS12");
		    ks.load(new FileInputStream("C:/tmp/keystore.p12"), "springboot".toCharArray());
		    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		    kmf.init(ks, "springboot".toCharArray());

		    SSLContext sc = SSLContext.getInstance("TLS");
		    sc.init(kmf.getKeyManagers(), null, null);

		    SSLServerSocketFactory ssf = sc.getServerSocketFactory();
		    SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(port);

		    try {
			while (true) {
			    // wait for the next client to connect and get its
			    // socket
			    // connection
			    Socket socket = serverSocket.accept();
			    // handle the socket connection by a handler in a
			    // new thread
			    new Thread(new SSLSocketHandler(socket, serverSocket)).start();
			}
		    } catch (IOException e) {
			System.err.println("Error while accepting connection on port " + port);
		    } finally {
			serverSocket.close();
		    }
		} catch (IOException e) {
		    System.err.println("Failed to bind to port " + port);
		} catch (KeyStoreException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		} catch (CertificateException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		} catch (UnrecoverableKeyException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		} catch (KeyManagementException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    }
	};
	Thread serverThread = new Thread(serverTask);
	serverThread.start();

    }

    public static void httpd(int port) {
	LOGGER.debug("Please open web browser and enter the url below:");
	LOGGER.debug(getAuthorizationUrl());
	try {
	    ServerSocket serverSocket = new ServerSocket(port);

	    try {
		while (true) {
		    // wait for the next client to connect and get its socket
		    // connection
		    Socket socket = serverSocket.accept();
		    // handle the socket connection by a handler in a new thread
		    new Thread(new SocketHandler(socket, serverSocket)).start();
		}
	    } catch (IOException e) {
		System.err.println("Error while accepting connection on port " + port);
	    } finally {
		serverSocket.close();
	    }
	} catch (IOException e) {
	    System.err.println("Failed to bind to port " + port);
	}
    }

    @SuppressWarnings("rawtypes")
    public static String[] requestTokenFromAuthCode(String code, String sessionstate) {
	HttpHeaders mheaders = new HttpHeaders();
	mheaders.add("Content-Type", "application/x-www-form-urlencoded");
	String body = "grant_type=authorization_code";
	body += "&code=%s";
	body += "&client_id=%s";
	body += "&session_state=%s";
	body += "&redirect_uri=" + CALLBACK_URL;
	body += "&client_secret=%s";
	HttpEntity<Object> request = new HttpEntity<Object>(String.format(body, code, ClientId, sessionstate, ClientSecret), mheaders);
	ResponseEntity<Map> response = null;
	RestTemplate restTemplate = initRestTemplate();
	try {
	    response = restTemplate.exchange(ACCESS_TOKEN_URL, HttpMethod.POST, request, Map.class);
	    LOGGER.debug("Access Token Response ---------" + response.getBody());
	    return new String[] { (String) response.getBody().get("access_token"), (String) response.getBody().get("refresh_token"),
	            (String) response.getBody().get("session_state"), new Gson().toJson(response.getBody()) };
	} catch (HttpClientErrorException e) {
	    LOGGER.error(e.getMessage());
	    return new String[] { "", "", "" };
	}
    }

    @SuppressWarnings("rawtypes")
    public static String[] refreshToken(String refreshToken) {
	HttpHeaders mheaders = new HttpHeaders();
	mheaders.add("Content-Type", "application/x-www-form-urlencoded");
	String body = "grant_type=refresh_token";
	body += "&client_id=%s";
	body += "&refresh_token=%s";
	;
	HttpEntity<Object> request = new HttpEntity<Object>(String.format(body, ClientId, refreshToken), mheaders);
	ResponseEntity<LinkedHashMap> response = null;
	RestTemplate restTemplate = initRestTemplate();
	try {
	    response = restTemplate.exchange(ACCESS_TOKEN_URL, HttpMethod.POST, request, LinkedHashMap.class);
	    LOGGER.debug("Refresh Token Response ---------" + response.getBody());
	    return new String[] { (String) response.getBody().get("access_token"), (String) response.getBody().get("refresh_token"),
	            (String) response.getBody().get("session_state"), new Gson().toJson(response.getBody()) };
	} catch (HttpClientErrorException e) {
	    LOGGER.error(e.getMessage());
	    return new String[] { "", "", "" };
	}
    }

    /**
     * to start a web browser
     * 
     * @param url
     */
    public static void browse(String url) {
	LOGGER.debug("Open web browser: {}", url);
	try {
	    if (Desktop.isDesktopSupported()) {
		Desktop desktop = Desktop.getDesktop();
		desktop.browse(new URI(url));
	    } else if (AppAndOsUtils.isWindows()) {
		Runtime runtime = Runtime.getRuntime();
		runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
	    } else {
		// Ubuntu
		Runtime runtime = Runtime.getRuntime();
		runtime.exec("xdg-open " + url);
	    }
	} catch (IOException | URISyntaxException e) {
	    LOGGER.debug(e.getMessage());
	    LOGGER.debug("Unable to start Webbroser. Please open webbroswer and visit: {}", getAuthorizationUrl());
	}
    }

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
}
