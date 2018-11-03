
public class MultiClientMain {

	public static void main(String[] args) {
		int port = 2423;
		
		SSLMultiClientServer server = new SSLMultiClientServer(port);
		server.start();
	}

}
