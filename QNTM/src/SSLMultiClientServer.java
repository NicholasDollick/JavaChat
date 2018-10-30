import java.io.*;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class SSLMultiClientServer
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
			
			/*
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
				int readInLen = inputStream.readInt();
				byte[] readIn;
				String message = "";
				
				if(readInLen > 0)
				{
					readIn = new byte[readInLen];
					inputStream.readFully(readIn, 0, readInLen);
					System.out.println(keyTest.toHexString(readIn));
					message = AES.decrypt(readIn, keyTest.getSecret());
				}

				System.out.println("client said: " + message);
				if(message.equals("CLOSE")) {
					//outputStream.writeUTF("Thank you for chatting!"); //this needs to be changed
					outputStream.close();
					inputStream.close();
					sslConnection.close();
					System.out.println("{*} Server shutting down");
					serverSocket.close();
				}
				else {
					byte[] messageOut = AES.encrypt("Server thinks you said: " + message, keyTest.getSecret());
					int len = messageOut.length;
					outputStream.writeInt(len);
					outputStream.write(messageOut);
				}
			} */
			
			while(true) {
				System.out.println("Waiting for client...");
				SSLSocket sslConnection = (SSLSocket) serverSocket.accept();
				System.out.println("Accepted connection from: " + sslConnection);
				SSLServerWorker worker = new SSLServerWorker(sslConnection);
				worker.start();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("{+} Server shut down");
	}
}