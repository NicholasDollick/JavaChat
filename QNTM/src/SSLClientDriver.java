import java.io.IOException;
import java.net.UnknownHostException;
import java.security.spec.InvalidKeySpecException;

public class SSLClientDriver {
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, InvalidKeySpecException, IOException {
		String message = "dino: its doing this check per message?";
		
		String[] test = message.split(" ");
		
		System.out.println(message);
		System.out.println(test[0].replaceAll(":", ""));
	}
}
