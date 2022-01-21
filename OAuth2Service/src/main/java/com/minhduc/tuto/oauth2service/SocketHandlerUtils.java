package com.minhduc.tuto.oauth2service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minhduc.tuto.oauth2service.http.HttpOauthService;
import com.minhduc.tuto.oauth2service.https.HttpsOauthService;

public class SocketHandlerUtils {
    private static final Logger LOGGER = LogManager.getLogger(SocketHandlerUtils.class);

    private static final Pattern REQUEST_PATTERN = Pattern.compile("^GET (/.*) HTTP/1.[01]$");

    /**
     * to extract a uri paramter value
     * 
     * @param path
     *            uri path
     * @param name
     *            the paramter name
     * @return the value of paramter
     */
    public static String extractUriParameter(String path, String name) {
	String REGEX = String.format(".*%s=([^&]+).*", name);
	Pattern pat = Pattern.compile(REGEX);
	Matcher mat = pat.matcher(path);
	if (mat.matches()) {
	    return mat.group(1);
	} else {
	    return null;
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
    public static long sendString(Socket socket, String str) throws IOException {
	LOGGER.debug("sendString(String str): {}", str);
	long len = str.length();
	OutputStream out = null;
	try {
	    out = sendResponseHeaders(socket, 200, "OK", len);
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
    public static OutputStream sendResponseHeaders(Socket socket, int status, String message, long len) throws IOException {
	StringBuffer response = new StringBuffer();
	response.append("HTTP/1.0 ");
	response.append(status).append(' ').append(message).append("\r\n");
	response.append("Content-Length: ").append(len).append("\r\n\r\n");
	OutputStream out = socket.getOutputStream();
	out.write(response.toString().getBytes());
	out.flush();
	return out;
    }

    public static String readRequestPath(Socket socket) throws IOException {
	BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	String firstLine = reader.readLine();
	if (firstLine == null) {
	    return null;
	}
	System.out.println("Firstline of Response: " + firstLine);

	Matcher matcher = REQUEST_PATTERN.matcher(firstLine);
	return matcher.matches() ? matcher.group(1) : null;
    }

    public static int sendErrorResponse(Socket socket, int status, String message) throws IOException {
	OutputStream out = SocketHandlerUtils.sendResponseHeaders(socket, status, message, message.length());
	out.write(message.getBytes());
	out.flush();
	return status;
    }

    public static String defaultResponse(String authorizationUrl) {
	String response = "<h1>Hello, I am a client of API Service</h1><h3><a href=\"%s\">Please authorize first</a></h3>";
	response = String.format(response, authorizationUrl);
	return response;
    }

    public static String authorizationCodeResponse(String code, String sessionstate) {
	String response = "<h1>Authorization Code</h1>" + "<h3>Grant success</h3>" + "<h3>code: %s</h3>" + "<h3>session_state: %s</h3>"
	        + "<a href=\"%s\">Get tokens now</a>";
	String getTokenUri = "/get_tokens?code=" + code + "&session_state=" + sessionstate;
	response = String.format(response, code, sessionstate, getTokenUri);
	return response;
    }

    public static String getTokenNowResponse(String[] tokens) {
	String response = "<h1>Token</h1>" + "<h3>access_token: %s</h3>" + "<h3>refresh_token: %s</h3>" + "<h3>session_state: %s</h3>";
	response = String.format(response, tokens[0], tokens[1], tokens[2]);

	response += "<a href=\"%s\">Refresh tokens now</a>";
	String refreshTokenUri = "/refresh_tokens?refresh_token=" + tokens[1];
	response = String.format(response, refreshTokenUri);

	response += "<h1>Complete OIDC Response:</h1>";
	response = String.format(response, refreshTokenUri);
	if (tokens.length > 3) {
	    response += tokens[3].replaceAll(",", ",<br/>");
	}
	return response;
    }

    public static String getTokenResponse(String[] tokens) {
	String response = "<h1>Get Token Successfully</h1>" + "<h3>access_token: </h3><p>%s<p/>" + "<h3>refresh_token:</h3><p>%s</p>"
	        + "<h3>session_state: </h3><p>%s</p>";
	response = String.format(response, tokens[0], tokens[1], tokens[2]);

	response += "<a href=\"%s\">Refresh tokens now</a><br/><h1>Complete Server Response:</h1>";
	String refreshTokenUri = "/refresh_tokens?refresh_token=" + tokens[1];
	response = String.format(response, refreshTokenUri);
	if (tokens.length > 3) {
	    response += tokens[3].replaceAll(",", ",<br/>");
	}
	return response;
    }

    public static String refreshTokenResponse(String[] tokens) {
	String response = "<h1>Token</h1>" + "<h3>access_token: </h3><p>%s<p/>" + "<h3>refresh_token:</h3><p>%s</p>"
	        + "<h3>session_state: </h3><p>%s</p>";
	response = String.format(response, tokens[0], tokens[1], tokens[2]);

	response += "<a href=\"%s\">Refresh tokens now</a><br/><h1>Complete Server Response:</h1>";
	String refreshTokenUri = "/refresh_tokens?refresh_token=" + tokens[1];
	response = String.format(response, refreshTokenUri);

	response += tokens[3].replaceAll(",", ",<br/>");
	return response;
    }
}
