package com.minhduc.tuto.oauth2service.httpserver;


import java.io.*;
import java.net.*;

public class ReverseServer {

    public static void main(String[] args) {
	int port = 8081;
	if (args.length < 1) {
	    
	} else {
	    port = Integer.parseInt(args[0]);
	}


	try (ServerSocket serverSocket = new ServerSocket(port)) {

	    System.out.println("Server is listening on port " + port);

	    while (true) {
		Socket socket = serverSocket.accept();
		System.out.println("New client connected");

		InputStream input = socket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));

		OutputStream output = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(output, true);

		String text;

		do {
		    text = reader.readLine();
		    String reverseText = new StringBuilder(text).reverse().toString();
		    writer.println("Server: " + reverseText);

		} while (!text.equals("bye"));

		socket.close();
	    }

	} catch (IOException ex) {
	    System.out.println("Server exception: " + ex.getMessage());
	    ex.printStackTrace();
	}
    }
}
