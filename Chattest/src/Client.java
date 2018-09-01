import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;

import javax.net.ssl.SSLSocket;
import javax.swing.*;


public class Client {
	String name = "QNTM v0.2";
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private SSLSocket connection;
	private String userName;
	JFrame chatFrame = new JFrame(name);
	JTextField messageBox;
	
	//constructor
	public Client(String host)
	{
		serverIP = host;
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		JPanel southPanel = new JPanel();
		southPanel.setBackground(Color.BLUE);
		southPanel.setLayout(new GridBagLayout());
		
		userText = new JTextField();
		//userText.setEditable(false);
		userText.requestFocusInWindow();
		
		JButton sendMessage = new JButton("Send Message");
		sendMessage.addActionListener(
				new sendMessageButtonActionListener());
		
		//old code, prob not needed
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
		
		JTextArea chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
		chatBox.setLineWrap(true);
		
		mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);
		
		GridBagConstraints left = new GridBagConstraints();
		left.anchor = GridBagConstraints.LINE_START;
		left.fill = GridBagConstraints.HORIZONTAL;
		left.weightx = 512.0D;
		left.weighty = 1.0D;
		
		GridBagConstraints right = new GridBagConstraints();
		right.insets = new Insets(0, 10, 0, 0);
		right.anchor = GridBagConstraints.LINE_END;
		right.fill = GridBagConstraints.NONE;
		right.weightx = 1.0D;
		right.weighty = 1.0D;
		
		southPanel.add(userText, left);
		southPanel.add(sendMessage, right);
	
		mainPanel.add(BorderLayout.SOUTH, southPanel);
		
		chatFrame.add(mainPanel);
		chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatFrame.setSize(600,480);
		chatFrame.setVisible(true);
		
		//chatWindow = new JTextArea();
		//add(new JScrollPane(chatWindow), BorderLayout.CENTER);
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
		connection = (SSLSocket) new Socket(InetAddress.getByName(serverIP), 2423);
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
	
	class sendMessageButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (messageBox.getText().length() < 1) {
				//empty message, do nothing
			}
			else if (messageBox.getText().equals(".clear")){
				userText.setText("All messages cleared\n"); //name may be wrong
				messageBox.setText("");
			}
			else {
				//this is where text gets updated
			}
	    
	
		}
	}
}
