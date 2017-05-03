import javax.swing.*;



public class customUser {

	public static void main(String[] args) 
	{
		String userName;

		userName = JOptionPane.showInputDialog("Enter a username: ", null);
		
		
		Client custom = new Client("127.0.0.1", userName);
		custom.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		custom.startRunning();
	}



}
