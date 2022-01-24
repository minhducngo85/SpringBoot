package com.minhduc.tuto.oauth2service.http;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minhduc.tuto.oauth2service.SocketHandlerUtils;

public class SocketHandler implements Runnable {

    /**
     * Logger object
     */
    private static final Logger LOGGER = LogManager.getLogger(SocketHandler.class);

    private final Socket socket;
    private ServerSocket server;

    private boolean closeServer = false;

    public SocketHandler(Socket socket, ServerSocket server) {
	this.socket = socket;
	this.server = server;
    }

    @Override
    public void run() {
	LOGGER.debug("SocketHandler started!");
	// initialize logging information
	String host = this.socket.getInetAddress().getHostName();
	String path = null;

	// handle request
	try {
	    path = SocketHandlerUtils.readRequestPath(socket);
	    // len>=0 if path is special path, else len<0 and visit local path
	    if (handleUriPaths(path) < 0) {
		SocketHandlerUtils.sendErrorResponse(socket, 404, "Not Found");
	    }
	    if (closeServer) {
		server.close();
	    }
	} catch (IOException e) {
	    System.err.println("Error while serving request for [" + path + "] from [" + host + "]: " + e.getMessage());
	    e.printStackTrace();
	} finally {
	    try {
		this.socket.close();
	    } catch (IOException e) {
		System.err.println("Error while closing socket to " + host + ": " + e.getMessage());
	    }
	}

    }

    /**
     * to handle uri from web broeser
     * 
     * @param path
     *            the uri path
     * @return the content length of httpo response
     * @throws IOException
     *             {@link IOException}
     */
    private long handleUriPaths(String path) throws IOException {
	String response = null;
	// handle request
	if (StringUtils.isEmpty(path) || path.equals("/")) { // default
	    response = "<h1>Hello, I am a client of API Service</h1><h3><a href=\"%s\">Please authorize first</a></h3>";
	    String authorizationUrl = HttpOauthService.getAuthorizationUrl();
	    response = String.format(response, authorizationUrl);
	    // handle authentication code
	} else if (path.startsWith("/?session_state") || path.startsWith("/?code") || path.startsWith("/auth_callback")) {
	    String code = SocketHandlerUtils.extractUriParameter(path, "code");
	    String sessionstate = SocketHandlerUtils.extractUriParameter(path, "session_state");
	    if (code == null)
		response = "Failed to get authentication code!";
	    else {
		response = SocketHandlerUtils.authorizationCodeResponse(code, sessionstate);

		boolean getTokenNow = false;
		if (getTokenNow) {
		    String[] tokens = HttpOauthService.requestTokenFromAuthCode(code, sessionstate);
		    response += SocketHandlerUtils.getTokenNowResponse(tokens);
		}
	    }
	} else if (path.startsWith("/get_tokens")) {
	    String code = SocketHandlerUtils.extractUriParameter(path, "code");
	    String sessionstate = SocketHandlerUtils.extractUriParameter(path, "session_state");
	    if (code == null)
		response = "Failed to get token!";
	    else {
		String[] tokens = HttpOauthService.requestTokenFromAuthCode(code, sessionstate);
		response = SocketHandlerUtils.getTokenResponse(tokens);

		// if you want to close server, set it = true
		closeServer = false;
	    }
	} else if (path.startsWith("/refresh_tokens")) {
	    String refresh_token = SocketHandlerUtils.extractUriParameter(path, "refresh_token");
	    if (refresh_token == null)
		response = "Failed to refresh token!";
	    else {
		String[] tokens = HttpOauthService.refreshToken(refresh_token);
		response = SocketHandlerUtils.refreshTokenResponse(tokens);
	    }
	}

	// send response back to browser
	if (response != null) {
	    return SocketHandlerUtils.sendString(socket, response);
	} else {
	    return -1;
	}
    }

}
