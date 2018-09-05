import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.swing.*;

import Client.sendMessageButtonActionListener;

/*
 * server class that communicates over SSL
 */

public class Server extends JFrame 
{
	String name = "QNTM v0.2";
	//private JTextField userText;
	private JTextArea chatWindow;
	private OutputStream output;
	private InputStream input;
	private String message = "";
	private String serverIP;
	private String port;
	private String username; //username is received from prechat login
	private BufferedReader bufferIn;
	
	JFrame chatFrame = new JFrame(name);
	JTextField messageBox; //where the user types
	static JTextArea chatBox;
	JButton sendMessage;
	JFrame preFrame;
	DH client = new DH(); // init DH algo
	DH otherClient = new DH();
    JTextField usernameBox;
    JPasswordField passwordBox;
	JTextField serverIPInput;
	JTextField portNum;
	
	private JTextField userText;

	private static ServerSocket server;
	private static Socket connection;
	
	
	//constructor
	public static void main(String [] args) throws InterruptedException {
		int port = 2423;
		System.out.println("[+] Starting Server");
		try {
			server = new ServerSocket(port);
			System.out.println("Waiting for client...");
			connection = server.accept();
			System.out.println("Accepted connection from: " + connection);
			DataInputStream din = new DataInputStream(connection.getInputStream());
			DataOutputStream dout = new DataOutputStream(connection.getOutputStream());
			
			String messageIn = "", messageOut = "";
			
			while(!messageIn.equals("CLOSE")) {
				messageIn = din.readUTF();
				chatBox.append(messageIn);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Main Chat Window
	 */
	public void chatWindow() throws NumberFormatException, UnknownHostException, IOException {
		connection = new Socket(InetAddress.getByName(serverIP), Integer.valueOf(port));
		input = connection.getInputStream();
		output = connection.getOutputStream();
		bufferIn = new BufferedReader(new InputStreamReader(input));
		// this block is for testing only
		//client.generateKeys();
		//otherClient.generateKeys();
		
    	//client.receivePublicKeyFrom(otherClient);
    	//otherClient.receivePublicKeyFrom(client);
    	
    	//client.getShared();
    	//otherClient.getShared();
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		JPanel southPanel = new JPanel();
		southPanel.setBackground(Color.BLUE);
		southPanel.setLayout(new GridBagLayout());
		
		messageBox = new JTextField(30);
		messageBox.requestFocusInWindow();
		
		sendMessage = new JButton("Send Message");
		sendMessage.addActionListener(
				new sendMessageButtonActionListener());
		
		chatBox = new JTextArea();
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
		
		southPanel.add(messageBox, left);
		southPanel.add(sendMessage, right);
	
		mainPanel.add(BorderLayout.SOUTH, southPanel);
		
		chatFrame.add(mainPanel);
		chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatFrame.setSize(600,480);
		chatFrame.setVisible(true);
	}
	
	/*
	 * PREWRITE NOTES
	 * 
	 * constructor generates keys
	 * 
	 * public key passed when connection established with client
	 * 
	 * constructor should be replaces with formatting from InitialLayout class
	 */
	
	
	//constructor
	public Server()
	{
		setTitle("test");
		userText = new JTextField(); 
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						sendMessage(event.getActionCommand());
						userText.setText("");
					}
				}
				);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(600,480);
		setVisible(true);
		
	}
	
	
	//set up and run the server
	public void startRunning()
	{
		try{
			server = (SSLServerSocket) new ServerSocket(2423, 3);
			while(true)
			{
				try{
					waitForConnection();
					setupStreams();
					whileChatting();
				}catch(EOFException eofException)
				{
					showMessage("\n Server ended the connection! ");
				}finally{
					closeSession();
				}
			}
		}catch(IOException ioException)
		{
			ioException.printStackTrace();
		}
	}
	
	//wait for connection, then display connection information
	private void waitForConnection() throws IOException
	{
		showMessage("Waiting for someone to connect...\n");
		connection = (SSLSocket) server.accept();
		showMessage("Now connected to: " + connection.getInetAddress().getHostAddress()); 
	}
	
	//get stream to send and receive the data
	private void setupStreams() throws IOException
	{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now setup! \n");
	}
	
	//during the chat conversation
	private void whileChatting() throws IOException
	{
		String message = " You are now connected!";
		sendMessage(message);
		ableToType(true);
		do
		{
			try{
				message = (String)input.readObject();
				showMessage("\n" + message);
				
			}catch(ClassNotFoundException classNotFoundException)
			{
				showMessage("\n This should never be displayed.");
			}
			//chat
		}while(!message.equals("CLIENT - END"));
	}
	
	//close streams and sockets
	private void closeSession()
	{
		showMessage("\n Closing conecction... \n");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException)
		{
			ioException.printStackTrace();
		}
	}
	
	//send message to client
	private void sendMessage(String message)
	{
		try{
			output.writeObject("SERVER - " + message);
			output.flush();
			showMessage("\n SERVER - " + message);
			
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
			else if (messageBox.getText().equals(".clear")) {
				chatBox.setText("All messages cleared\n");
				messageBox.setText("");
			}
			else {
				try {
					output.write(("<" + username + ">" + messageBox.getText()
							+ "\n").getBytes());
					output.flush();
					chatBox.append("<" + username + ">" + messageBox.getText()
							+ "\n");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				messageBox.setText("");
			}
			messageBox.requestFocusInWindow();
		}
	}

}