import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.Date;
import java.awt.event.*;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.*;

public class ClientTest
{
	//private static ServerSocket server;
	//private static Socket connection;
	
	
	//constructor
	public static void main(String [] args) throws InterruptedException {
		int port = 2423;
		//configure ssl
		System.setProperty("javax.net.ssl.trustStore", "myTrustStore.jts");
		System.setProperty("javax.net.ssl.trustStorePassword", "asdf123");
		System.setProperty("javax.net.debug", "ssl");
		try {
			SSLSocketFactory sockFact = (SSLSocketFactory)SSLSocketFactory.getDefault();
			SSLSocket sslSocket = (SSLSocket)sockFact.createSocket("localhost",port); //something about inet addy might be neded
			DataOutputStream outputStream = new DataOutputStream(sslSocket.getOutputStream());
			DataInputStream inputStream = new DataInputStream(sslSocket.getInputStream());
			System.out.println(inputStream.readUTF());
			while(true) {
				System.out.print("Write a message: ");
				String out = System.console().readLine();
				outputStream.writeUTF(out);
				System.err.println(inputStream.readUTF());
				if(out.equals("CLOSE")) {
					break;
				}				
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