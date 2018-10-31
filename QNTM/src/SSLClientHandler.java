import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocket;

public class SSLClientHandler extends Thread {
	private final SSLSocket clientSocket;
	private final SSLMultiClientServer server;
	private String login = null;
	private ArrayList totalClients;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;

	public SSLClientHandler(SSLMultiClientServer server, SSLSocket sslConnection) {
		this.clientSocket = sslConnection;
		this.server = server;
	}
	
	public void setClients(ArrayList clients) {
		this.totalClients = clients;
	}
	
	public void run() {
		try {
			handleClientConnection();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendMessage(String msg) throws IOException {
		outputStream.writeUTF(msg);
	}
	
	private void handleClientConnection() throws IOException {
		this.outputStream = new DataOutputStream(clientSocket.getOutputStream());
		this.inputStream = new DataInputStream(clientSocket.getInputStream());
		try {
			/*
			System.out.println("Starting key exchange");
			
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
		*/
		
		while(true) {
			//int readInLen = inputStream.readInt();
			//byte[] readIn;
			String message = "";
			
			/*
			if(readInLen > 0)
			{
				readIn = new byte[readInLen];
				inputStream.readFully(readIn, 0, readInLen);
				//System.out.println(keyTest.toHexString(readIn));
				//message = AES.decrypt(readIn, keyTest.getSecret());
				message = readIn.toString();
			} */
			
			
			message = inputStream.readUTF();
			System.out.println("client said: " + message);
			
			if(message.equals("CLOSE")) {
				//outputStream.writeUTF("Thank you for chatting!"); //this needs to be changed
				outputStream.close();
				inputStream.close();
				clientSocket.close();
				System.out.println("{*} Server shutting down");
				//this.close();
			}
			else {
				//byte[] messageOut = AES.encrypt("Server thinks you said: " + message, keyTest.getSecret());
				/*
				byte[] messageOut = (message).getBytes();
				int len = messageOut.length;
				outputStream.writeInt(len);
				outputStream.write(messageOut);
				*/
				List<SSLClientHandler> clientList = server.getClients();
				for(SSLClientHandler client : clientList)
					client.sendMessage(message);
				
			}
		}
	
	} catch(Exception e) {
		e.printStackTrace();
	}
}
}
