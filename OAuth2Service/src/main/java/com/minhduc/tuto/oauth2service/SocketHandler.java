package com.minhduc.tuto.oauth2service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SocketHandler implements Runnable {

    /**
     * Logger object
     */
    private static final Logger LOGGER = LogManager.getLogger(SocketHandler.class);

    private static final Pattern REQUEST_PATTERN = Pattern.compile("^GET (/.*) HTTP/1.[01]$");
    private final Socket socket;
    private ServerSocket server;

    private boolean closeServer = false;

    public SocketHandler(Socket socket, ServerSocket server) {
	this.socket = socket;
	this.server = server;
    }

    @Override
    public void run() {
	// initialize logging information
	String host = this.socket.getInetAddress().getHostName();
	String path = null;

	// handle request
	try {
	    path = readRequestPath();
	    // len>=0 if path is special path, else len<0 and visit local path
	    if (handleUriPaths(path) < 0) {
		sendErrorResponse(404, "Not Found");
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

    private String readRequestPath() throws IOException {
	BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
	String firstLine = reader.readLine();
	if (firstLine == null) {
	    return null;
	}
	System.out.println("Firstline of Response: " + firstLine);

	Matcher matcher = REQUEST_PATTERN.matcher(firstLine);
	return matcher.matches() ? matcher.group(1) : null;
    }

    private int sendErrorResponse(int status, String message) throws IOException {
	OutputStream out = sendResponseHeaders(status, message, message.length());
	out.write(message.getBytes());
	out.flush();
	return status;
    }

    /**
     * to send header in http response
     * 
     * @param status
     *            the status code
     * @param message
     *            the message
     * @param len
     *            the length of content
     * @return output stream
     * @throws IOException
     *             {@link IOException}
     */
    private OutputStream sendResponseHeaders(int status, String message, long len) throws IOException {
	StringBuffer response = new StringBuffer();
	response.append("HTTP/1.0 ");
	response.append(status).append(' ').append(message).append("\r\n");
	response.append("Content-Length: ").append(len).append("\r\n\r\n");
	OutputStream out = this.socket.getOutputStream();
	out.write(response.toString().getBytes());
	out.flush();
	return out;
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
	} else if (path.startsWith("/auth_callback")) { // handle authentication
	                                                // code
	    String code = extractUriParameter(path, "code");
	    String sessionstate = extractUriParameter(path, "session_state");
	    if (code == null)
		response = "Failed to get authentication code!";
	    else {
		response = "<h1>Authorization Code</h1>" + "<h3>Grant success</h3>" + "<h3>code: %s</h3>" + "<h3>session_state: %s</h3>"
		        + "<a href=\"%s\">Get tokens now</a>";
		String getTokenUri = "/get_tokens?code=" + code + "&session_state=" + sessionstate;
		response = String.format(response, code, sessionstate, getTokenUri);
		boolean getTokenNow = false;
		if (getTokenNow) {
		    String[] tokens = HttpOauthService.requestTokenFromAuthCode(code, sessionstate);
		    response += "<h1>Token</h1>" + "<h3>access_token: %s</h3>" + "<h3>refresh_token: %s</h3>" + "<h3>session_state: %s</h3>";
		    response = String.format(response, tokens[0], tokens[1], tokens[2]);

		    response += "<a href=\"%s\">Refresh tokens now</a>";
		    String refreshTokenUri = "/refresh_tokens?refresh_token=" + tokens[1];
		    response = String.format(response, refreshTokenUri);

		    response += "<h1>Complete OIDC Response:</h1>";
		    response = String.format(response, refreshTokenUri);

		    response += tokens[3].replaceAll(",", ",<br/>");
		}
	    }
	} else if (path.startsWith("/get_tokens")) {
	    String code = extractUriParameter(path, "code");
	    String sessionstate = extractUriParameter(path, "session_state");
	    if (code == null)
		response = "Failed to get token!";
	    else {
		// String[] tokens = ClientExample.getToken_from_code(code);
		String[] tokens = HttpOauthService.requestTokenFromAuthCode(code, sessionstate);
		response = "<h1>Get Token Successfully</h1>" + "<h3>access_token: </h3><p>%s<p/>" + "<h3>refresh_token:</h3><p>%s</p>"
		        + "<h3>session_state: </h3><p>%s</p>";
		response = String.format(response, tokens[0], tokens[1], tokens[2]);

		response += "<a href=\"%s\">Refresh tokens now</a><br/><h1>Complete Server Response:</h1>";
		String refreshTokenUri = "/refresh_tokens?refresh_token=" + tokens[1];
		response = String.format(response, refreshTokenUri);
		if (tokens.length >= 4) {
		    response += tokens[3].replaceAll(",", ",<br/>");
		}

		// if you want to close server, set it = true
		closeServer = false;
	    }
	} else if (path.startsWith("/refresh_tokens")) {
	    String refresh_token = extractUriParameter(path, "refresh_token");
	    if (refresh_token == null)
		response = "Failed to refresh token!";
	    else {
		// String[] tokens = ClientExample.getToken_from_code(code);
		String[] tokens = HttpOauthService.refreshToken(refresh_token);

		response = "<h1>Token</h1>" + "<h3>access_token: </h3><p>%s<p/>" + "<h3>refresh_token:</h3><p>%s</p>"
		        + "<h3>session_state: </h3><p>%s</p>";

		response = String.format(response, tokens[0], tokens[1], tokens[2]);

		response += "<a href=\"%s\">Refresh tokens now</a><br/><h1>Complete Server Response:</h1>";
		String refreshTokenUri = "/refresh_tokens?refresh_token=" + tokens[1];
		response = String.format(response, refreshTokenUri);

		response += tokens[3].replaceAll(",", ",<br/>");
	    }
	}

	// send response back to browser
	if (response != null) {
	    return sendString(response);
	} else {
	    return -1;
	}
    }

    /**
     * to send a response with status code 200
     * 
     * @param str
     *            the content
     * @return a http message
     * @throws IOException
     *             {@link IOException}
     */
    private long sendString(String str) throws IOException {
	LOGGER.debug("sendString(String str): {}", str);
	long len = str.length();
	OutputStream out = null;
	try {
	    out = sendResponseHeaders(200, "OK", len);
	    out.write(str.getBytes());
	    out.flush();
	} catch (SocketException e) {
	    LOGGER.error(e.getMessage());
	} finally {
	    if (out != null) {
		out.close();
	    }
	}
	return len;
    }

    /**
     * to extract a uri paramter value
     * 
     * @param path
     *            uri path
     * @param name
     *            the paramter name
     * @return the value of paramter
     */
    private String extractUriParameter(String path, String name) {
	String REGEX = String.format(".*%s=([^&]+).*", name);
	Pattern pat = Pattern.compile(REGEX);
	Matcher mat = pat.matcher(path);
	if (mat.matches()) {
	    return mat.group(1);
	} else {
	    return null;
	}
    }
}
