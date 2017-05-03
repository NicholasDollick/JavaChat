import javax.swing.*;


public class ClientTest
{

	public static void main(String[] args) 
	{
		Client testy = new Client("72.199.66.239");
		testy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testy.startRunning();
		
		

	}

}
