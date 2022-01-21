package com.minhduc.tuto.oauth2service.httpserver;


import java.io.*;
import java.net.*;

/**
 * This program demonstrates a simple TCP/IP socket client that reads input from
 * the user and prints echoed message from the server.
 *
 * @author www.codejava.net
 */
public class ReverseClient {

    public static void main(String[] args) {
	//if (args.length < 2)
	//    return;

	String hostname = "localhost";
	int port = 8081;

	try (Socket socket = new Socket(hostname, port)) {

	    OutputStream output = socket.getOutputStream();
	    PrintWriter writer = new PrintWriter(output, true);

	    Console console = System.console();
	    String text = "hello world";

	    do {
		//text = console.readLine("Enter text: ");

		writer.println(text);

		InputStream input = socket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));

		String time = reader.readLine();

		System.out.println(time);

	    } while (!text.equals("bye"));

	    socket.close();

	} catch (UnknownHostException ex) {

	    System.out.println("Server not found: " + ex.getMessage());

	} catch (IOException ex) {

	    System.out.println("I/O error: " + ex.getMessage());
	}
    }
}
