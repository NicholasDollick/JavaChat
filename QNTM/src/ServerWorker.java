import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ServerWorker extends Thread {
	private final Socket clientSocket;
	private String login = null;
	
	public ServerWorker(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	public void run() {
		try {
			handleClientSocket();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void handleClientSocket() throws IOException, InterruptedException {
		//ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
		DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
		DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
		
		//BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		
		String msgIn = "";
		
		while(!msgIn.equals("CLOSE")) {
			if ("quit".equalsIgnoreCase(msgIn)) {
				break;
			}
			else {
				String msg = msgIn + "\n";
				outputStream.write(msg.getBytes());
				outputStream.flush();
			}
		}
		
		clientSocket.close();
	}
	
	private void handleUser(OutputStream outputStream, String[] args) { 
		if(args.length == 2) {
			String login = args[0];
			String password = args[1];
		}
	}
	
	private void handleMessage(String[] args) { 
		String sendTo = args[1];
		String body = args[0];
		
	}

}
