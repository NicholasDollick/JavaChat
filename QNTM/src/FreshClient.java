import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class FreshClient extends Thread {
	private String username;
	private int port;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private byte[] pubKey;
	private byte[] privKey;
	private DH dh = new DH();

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
			initKeys();
			this.outputStream = new DataOutputStream(sslSocket.getOutputStream());
			this.inputStream = new DataInputStream(sslSocket.getInputStream());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initKeys() {
		try {
			dh.generateKeys();
			this.pubKey = dh.getPublicKeyEnc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendKey() {
		int len = pubKey.length;
		try {
			outputStream.writeInt(len);
			outputStream.write(pubKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void receiveKey() {
		try {
			int len = inputStream.read();
			if(len > 0) {
				byte[] m = new byte[len];
				inputStream.readFully(m, 0, m.length);
				dh.receivePublicKey(m);
				dh.getShared();
				this.privKey = dh.getSecret();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageUTF(String message) {
		try {
			outputStream.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageBytes(byte[] message) {
		try {
			outputStream.writeInt(message.length);
			outputStream.write(message);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getMessage() throws IOException {
		String message = inputStream.readUTF();
		
		if(message.equals("serverRequestKey")) {
			sendKey();
			return "{*} Sent Client Public Key";
		}
		
		return message;
	}
	
}
