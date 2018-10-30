import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class SSLServer
{
	//constructor
	public static void main(String [] args) throws InterruptedException {
		int port = 2423;
		DH keyTest = new DH();
		
		//configure ssl
		System.setProperty("javax.net.ssl.keyStore", "myKeyStore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "asdf123");
		//System.setProperty("javax.net.debug", "ssl");
		try {
			SSLServerSocketFactory serverSockFact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
			System.out.println("{*} Starting Server");
			SSLServerSocket serverSocket = (SSLServerSocket)serverSockFact.createServerSocket(port); //something about inet addy might be neded
			SSLSocket sslConnection = (SSLSocket) serverSocket.accept();
			System.out.println("Accepted connection from: " + sslConnection);
			DataOutputStream outputStream = new DataOutputStream(sslConnection.getOutputStream());
			DataInputStream inputStream = new DataInputStream(sslConnection.getInputStream());
			
			//small test of DH key exchange between sockets
			try {
				System.out.println("Starting key exchange");
				// generate keys
				keyTest.generateKeys();
				System.out.println(keyTest.toHexString(keyTest.getPublicKeyEnc()));
				
				// send public key to client
				System.out.println("{*} Sending Key");
				byte[] pubKey = keyTest.getPublicKeyEnc();
				int len = pubKey.length;
				outputStream.writeInt(len);
				outputStream.write(pubKey);
				System.out.println("{+} Key Sent");
				
				// recieve client key
				System.out.println("{*} Waiting For Key");
				len = inputStream.readInt();
				if(len > 0) {
					byte[] m = new byte[len];
					inputStream.readFully(m, 0, m.length);
					keyTest.receivePublicKey(m);
				}
				System.out.println("{+} Got Key");
				
				
				// generate secret
				System.out.println("{*} Generating...");
				keyTest.getShared();
				
				System.out.println(keyTest.toHexString(keyTest.getSecret()));
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
			

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
		System.out.println("{+} Server shut down");
	}
}