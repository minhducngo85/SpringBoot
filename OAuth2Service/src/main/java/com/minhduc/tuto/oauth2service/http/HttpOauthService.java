package com.minhduc.tuto.oauth2service.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.scribejava.core.utils.OAuthEncoder;
import com.minhduc.tuto.oauth2service.AppAndOsUtils;
import com.minhduc.tuto.oauth2service.RestTemplateUtils;

public class HttpOauthService {

    /**
     * Logger object
     */
    private static final Logger LOGGER = LogManager.getLogger(HttpOauthService.class);

    /** Authorize and token endpoint */
    private static final String AUTHORIZE_URL = "http://localhost:8080/auth/realms/SpringBootRealm/protocol/openid-connect/auth?response_type=code&client_id=%s&redirect_uri=%s";
    private static final String ACCESS_TOKEN_URL = "http://localhost:8080/auth/realms/SpringBootRealm/protocol/openid-connect/token";

    /** Client information */
    private static final String ClientId = "login-app";
    public static final String ClientSecret = "client_secret";
    private static int port = 9200;
    private static final String SERVER_URL = "http://localhost:" + port;
    private static final String CALLBACK_URL = SERVER_URL + "/auth_callback";

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
	new HttpOauthService().startServer();
	LOGGER.debug("HttpOauthService started! Opening webbrowser");
	AppAndOsUtils.browse(SERVER_URL);
    }

    public void startServer() {
	Runnable serverTask = new Runnable() {
	    @Override
	    public void run() {
		try {
		    ServerSocket serverSocket = new ServerSocket(port);

		    try {
			while (true) {
			    // wait for the next client to connect and get its
			    // socket
			    // connection
			    Socket socket = serverSocket.accept();
			    // handle the socket connection by a handler in a
			    // new thread
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
	};
	Thread serverThread = new Thread(serverTask);
	serverThread.start();

    }

    public static String[] requestTokenFromAuthCode(String code, String sessionstate) {
	return RestTemplateUtils.requestTokenFromAuthCode(code, sessionstate, ClientId, ClientSecret, CALLBACK_URL, ACCESS_TOKEN_URL);
    }

    /**
     * to refresh token
     * 
     * @param refreshToken
     * @return
     */
    public static String[] refreshToken(String refreshToken) {
	return RestTemplateUtils.refreshToken(refreshToken, ClientId, ACCESS_TOKEN_URL);
    }

}
