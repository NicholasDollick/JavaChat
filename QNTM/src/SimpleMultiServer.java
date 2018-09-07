import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleMultiServer {
	private static ServerSocket server;
	private static Socket connection;
	static DataInputStream din;
	static DataOutputStream dout;
	
	public static void main(String[] args) {
		int port = 2423;
		
		System.out.println("[+] Starting Server");
		try {
			String messageIn = "", messageOut = "";
			server = new ServerSocket(port);
			while(true) {
				System.out.println("[*] Waiting for clients");
				connection = server.accept();
				System.out.println("Accepted connection from: " + connection);
				ServerWorker worker = new ServerWorker(connection);
				worker.start();
			}
			
				
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
