import javax.swing.*;

public class Servertest 
{

	public static void main(String[] args) 
	{
		SimpleServer test = new SimpleServer();
		
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.startRunning();

	}

}