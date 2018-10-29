import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.Date;
import java.awt.event.*;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.swing.*;

public class SSLServer
{
	//private static ServerSocket server;
	//private static Socket connection;
	
	
	//constructor
	public static void main(String [] args) throws InterruptedException {
		int port = 2423;
		//configure ssl
		System.setProperty("javax.net.ssl.keyStore", "myKeyStore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "asdf123");
		System.setProperty("javax.net.debug", "ssl");
		try {
			SSLServerSocketFactory serverSockFact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
			System.out.println("{*} Starting Server");
			SSLServerSocket serverSocket = (SSLServerSocket)serverSockFact.createServerSocket(port); //something about inet addy might be neded
			SSLSocket sslConnection = (SSLSocket) serverSocket.accept();
			System.out.println("Accepted connection from: " + sslConnection);
			DataOutputStream outputStream = new DataOutputStream(sslConnection.getOutputStream());
			DataInputStream inputStream = new DataInputStream(sslConnection.getInputStream());
			while(true) {
				String in = inputStream.readUTF();
				System.out.println("client said: " + in);
				if(in.equals("CLOSE")) {
					outputStream.writeUTF("Thank you for chatting!");
					outputStream.close();
					inputStream.close();
					sslConnection.close();
					System.out.println("{*} Server shutting down");
					serverSocket.close();
				}
				else
					outputStream.writeUTF("you said: " + in);
				
				
			}
			/*
			while(true) {
				System.out.println("Waiting for client...");
				System.out.println("Accepted connection from: " + connection);
				ServerWorker worker = new ServerWorker(connection);
				worker.start();
			}
			*/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}