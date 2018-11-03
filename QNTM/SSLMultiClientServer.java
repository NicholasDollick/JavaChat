import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class SSLMultiClientServer extends Thread
{
	private final int port;
	private ArrayList<SSLClientHandler> clientList  = new ArrayList<>();
	
	public SSLMultiClientServer(int serverPort) {
		this.port = serverPort;
	}
	
	public List<SSLClientHandler> getClients() {
		return clientList;
	}
	
	public void run() {
		//configure ssl
		System.setProperty("javax.net.ssl.keyStore", "myKeyStore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "asdf123");
		System.setProperty("javax.net.debug", "ssl");
		try {
			SSLServerSocketFactory serverSockFact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
			System.out.println("{*} Starting Server");
			SSLServerSocket serverSocket = (SSLServerSocket)serverSockFact.createServerSocket(port);
			while(true) {
				System.out.println("{*} Waiting for clients...");
				SSLSocket sslConnection = (SSLSocket) serverSocket.accept();
				System.out.println("{+} Accepted connection from: " + sslConnection);
				SSLClientHandler handler = new SSLClientHandler(this, sslConnection);
				clientList.add(handler);
				handler.start();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}