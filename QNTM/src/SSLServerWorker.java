import javax.net.ssl.SSLSocket;

public class SSLServerWorker extends Thread {
	private final SSLSocket clientSocket;
	private String login = null;

	public SSLServerWorker(SSLSocket sslConnection) {
		this.clientSocket = sslConnection;
	}
	
	public void run() {
		try {
			handleClientConnection();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void handleClientConnection() {
		
	}
}
