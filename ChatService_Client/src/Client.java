import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;


@SuppressWarnings("serial")
public class Client extends JFrame 
{
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	private String userName;
	
	//constructor
	public Client(String host)
	{
		setTitle("Client-Side Test");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						sendData(event.getActionCommand());
						userText.setText("");
					}
				}
		);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(600,480);
		setVisible(true);
	}
	
	//custom username constructor
	public Client(String host, String username)
	{
		
		setTitle("Client-Side Test");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userName = username;
		userText.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						sendData(event.getActionCommand(), userName);
						userText.setText("");
					}
				}
		);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(600,480);
		setVisible(true);
	}
	
	//connect to server
	public void startRunning()
	{
		try{
			connectToServer();
			setupStreams();
			whileChatting();
		}catch(EOFException eofException){
			showMessage("\n Client terminated connection");
		}catch(IOException ioException){
			ioException.printStackTrace();
		}finally{
			closeSession();
		}
	}
	
	//Custom Username constructor
	public void startRunning(String name)
	{
		try{
			connectToServer();
			setupStreams();
			whileChatting(name);
		}catch(EOFException eofException){
			showMessage("\n Client terminated connection");
		}catch(IOException ioException){
			ioException.printStackTrace();
		}finally{
			closeSession();
		}
	}
	

	//connect to server
	private void connectToServer() throws IOException
	{
		showMessage("Attempting connection...\n");
		connection = new Socket(InetAddress.getByName(serverIP), 2423);
		showMessage("Connected to: " + connection.getInetAddress().getHostAddress());
	}
	
	//setup streams to send/receive data
	private void setupStreams() throws IOException
	{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n You're Connected! \n");
	}
	
	//while chatting with server
	private void whileChatting() throws IOException
	{
		ableToType(true);
		do{
			try{
				message = (String) input.readObject();
				showMessage("\n" + message);
				
			}catch(ClassNotFoundException classNotFoundException){
				showMessage("\n This really should never be displayed \n");
			}
			
		}while(!message.equals("SERVER - END"));
	}
	
	//while chatting with server
	private void whileChatting(String username) throws IOException
	{
		ableToType(true);
		do{
			try{
				message = (String) input.readObject();
				//showMessage("\n" + username + "Says: \n" + message);
				showMessage("\n" + message);
				
			}catch(ClassNotFoundException classNotFoundException){
				showMessage("\n This really should never be displayed \n");
			}
			
		}while(!message.equals("SERVER - END"));
	}
	
	//closes streams and sockets
	private void closeSession()
	{
		showMessage("\n Shutting Down \n");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

	//send message to client
	private void sendData(String message)
	{
		try{
			output.writeObject("CLIENT - " + message);
			output.flush();
			showMessage("\n CLIENT - " + message);
			
		}catch(IOException ioException){
			chatWindow.append("\n ERROR: Message Cannot Be Sent \n");
		}
	}
	
	//custom user
	private void sendData(String message, String name)
	{
		try{
			output.writeObject(name + " says:  \n" + message);
			output.flush();
			showMessage("\n" + name + " says \n " + message);
			
		}catch(IOException ioException){
			chatWindow.append("\n ERROR: Message Cannot Be Sent \n");
		}
	}
	
	//updates chatWindow
	private void showMessage(final String text)
	{
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				chatWindow.append(text);
			}
		}
				);
	}
	
	//change user privileges
	private void ableToType(final boolean tof)
	{
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				userText.setEditable(tof);
			}
		}
				);
		
	}
}
