import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.Date;
import java.awt.event.*;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.swing.*;

public class SecureServer
{
	private static ServerSocket server;
	private static Socket connection;
	
	
	//constructor
	public static void main(String [] args) throws InterruptedException {
		int port = 8080;
		try {
			server = new ServerSocket(port);
			while(true) {
				System.out.println("Waiting for client...");
				connection = server.accept();
				System.out.println("Accepted connection from: " + connection);
				ServerWorker worker = new ServerWorker(connection);
				worker.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}