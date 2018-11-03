import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLSocket;

public class SSLClientHandler extends Thread {
	private final SSLSocket clientSocket;
	private final SSLMultiClientServer server;
	private String login = null;
	private List<SSLClientHandler> totalClients;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	private boolean isEncrypt = true;

	public SSLClientHandler(SSLMultiClientServer server, SSLSocket sslConnection) {
		this.clientSocket = sslConnection;
		this.server = server;
	}
	
	public void setClients(ArrayList<SSLClientHandler> clients) {
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
			while(true) {
				String message = "";
				
				message = inputStream.readUTF();
				
				if(message.equals("CLOSE")) {
					//outputStream.writeUTF("Thank you for chatting!"); //this needs to be changed
					outputStream.close();
					inputStream.close();
					clientSocket.close();
					System.out.println("{*} Server shutting down");
				}
				else {
					System.out.println(LocalDateTime.now().toString() + " " + message); //Message Logging
					
					List<SSLClientHandler> clientList = server.getClients();
					for(SSLClientHandler client : clientList)
						client.sendMessage(message);
				}
			}
		
		//byte[] messageOut = AES.encrypt("Server thinks you said: " + message, keyTest.getSecret());
		/*
		byte[] messageOut = (message).getBytes();
		int len = messageOut.length;
		outputStream.writeInt(len);
		outputStream.write(messageOut);
		*/
		
		/*
		totalClients = server.getClients();
		if (totalClients.size() == 2)
			if(isEncrypt)
			{
				ArrayList<byte[]> keys = new ArrayList<>();
				totalClients = server.getClients();
				System.out.println("Number of Clients: " + totalClients.size());
				System.out.println(totalClients.get(0).hashCode());
				System.out.println(totalClients.get(1).hashCode());
			}
		
		
		if(readInLen > 0)
		{
			readIn = new byte[readInLen];
			inputStream.readFully(readIn, 0, readInLen);
			//System.out.println(keyTest.toHexString(readIn));
			//message = AES.decrypt(readIn, keyTest.getSecret());
			message = readIn.toString();
			
								for(SSLClientHandler client : totalClients)
				{
					client.sendMessage("serverRequestKey");
					System.out.println("{*} Sent Request");
					byte [] readIn;
					int readInLen = inputStream.readInt();	
					System.out.println(readInLen);
					readIn = new byte[readInLen];
					System.out.println("READFULLY");
					inputStream.readFully(readIn, 0, readInLen);
					keys.add(readIn);
					System.out.println(keys.size());
				}
				isEncrypt = false;
		} */
	
		} catch(Exception e) {
			e.printStackTrace();
	}
}
}
