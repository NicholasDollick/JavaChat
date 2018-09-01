import javax.swing.*;

public class Servertest 
{

	public static void main(String[] args) 
	{
		Server test = new Server();
		
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.startRunning();

	}

}