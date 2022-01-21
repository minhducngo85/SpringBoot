package com.minhduc.tuto.oauth2service.https;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.scribejava.core.utils.OAuthEncoder;
import com.minhduc.tuto.oauth2service.AppAndOsUtils;
import com.minhduc.tuto.oauth2service.RestTemplateUtils;

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
    public static final String ClientSecret = "";
    private static int port = 9200;
    private static final String SERVER_URL = "https://localhost:" + port;
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
	new HttpsOauthService().startServer();
	LOGGER.debug("(Secure) HttpsOauthService started! Opening webbrowser");
	AppAndOsUtils.browse(SERVER_URL);
    }

    /**
     * start socket server on a separate thread
     */
    public void startServer() {
	Runnable serverTask = new Runnable() {
	    @Override
	    public void run() {
		try {
		    // ServerSocket serverSocket = new ServerSocket(port);
		    KeyStore ks = KeyStore.getInstance("PKCS12");

		    URL resource = HttpsOauthService.class.getClassLoader().getResource("keystore.p12");
		    String filepath = Paths.get(resource.toURI()).toFile().getAbsolutePath();
		    LOGGER.debug("Keystore: {}", filepath);
		    ks.load(new FileInputStream(filepath), "springboot".toCharArray());

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
		} catch (URISyntaxException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
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
