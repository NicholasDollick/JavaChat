import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class FreshClient extends Thread {
	private String username;
	private int port;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private String msg;

	public FreshClient(int port, String username) {
		this.port = port;
		this.username = username;
	}
	
	//connections client to server
	public void run() {
		System.setProperty("javax.net.ssl.trustStore", "myTrustStore.jts");
		System.setProperty("javax.net.ssl.trustStorePassword", "asdf123");
		try {
			SSLSocketFactory sockFact = (SSLSocketFactory)SSLSocketFactory.getDefault();
			SSLSocket sslSocket = (SSLSocket)sockFact.createSocket("localhost",port); //something about inet addy might be neded
			this.outputStream = new DataOutputStream(sslSocket.getOutputStream());
			this.inputStream = new DataInputStream(sslSocket.getInputStream());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageUTF(String message) {
		try {
			outputStream.writeUTF(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DataInputStream getReader() {
		return inputStream;
	}
	
	public String getMessage() throws IOException {
		return inputStream.readUTF();
	}
	
}
