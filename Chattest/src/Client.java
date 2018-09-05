import java.io.*;
import java.awt.*;
import java.net.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.awt.event.*;
import javax.net.ssl.SSLSocket;
import javax.swing.*;


public class Client {
	String name = "QNTM v0.2";
	//private JTextField userText;
	private JTextArea chatWindow;
	private OutputStream output;
	private InputStream input;
	private String message = "";
	private String serverIP;
	private String port;
	private Socket connection;
	private String username; //username is received from prechat login
	
	JFrame chatFrame = new JFrame(name);
	JTextField messageBox; //where the user types
	JTextArea chatBox;
	JButton sendMessage;
	JFrame preFrame;
	DH client = new DH(); // init DH algo
	DH otherClient = new DH();
    JTextField usernameBox;
    JPasswordField passwordBox;
	JTextField serverIPInput;
	JTextField portNum;
	
	//constructor
	public Client() throws InvalidKeyException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException
	{
		// match UI of user desktop environment
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
    	serverIPInput = new JTextField(15);
    	portNum = new JTextField(4);
    	JLabel enterServerIP = new JLabel("Server IP:");
    	JLabel port = new JLabel("Port #:");
    	chatFrame.setVisible(false);
    	preFrame = new JFrame(name);
    	usernameBox = new JTextField(15);
    	passwordBox = new JPasswordField(15);
    	JLabel chooseUsernameLabel = new JLabel("username:");
    	JLabel choosePassword = new JLabel("password:");
    	JButton enterServer = new JButton("Enter Chat Server");
    	JButton createAccount = new JButton("Create Account");
    	enterServer.addActionListener(new enterServerButtonListener());
    	JPanel prePanel = new JPanel(new GridBagLayout());
    	
    	GridBagConstraints preRight = new GridBagConstraints();
        preRight.insets = new Insets(0, 0, 10, 10);
        preRight.anchor = GridBagConstraints.EAST;
        GridBagConstraints preLeft = new GridBagConstraints();
        preLeft.anchor = GridBagConstraints.WEST;
        preLeft.insets = new Insets(0, 10, 10, 10);
        preRight.fill = GridBagConstraints.HORIZONTAL;
        preRight.gridwidth = GridBagConstraints.REMAINDER;

        prePanel.add(chooseUsernameLabel, preLeft);
        prePanel.add(usernameBox, preRight);
        //prePanel.add(choosePassword, preLeft);
        //prePanel.add(passwordBox, preRight);
        prePanel.add(enterServerIP, preLeft);
        prePanel.add(serverIPInput, preRight);
        prePanel.add(port, preLeft);
        prePanel.add(portNum, preRight);
        preFrame.add(prePanel, BorderLayout.CENTER);
        preFrame.add(createAccount, BorderLayout.SOUTH);
        preFrame.add(enterServer, BorderLayout.SOUTH);
        preFrame.setSize(350, 350);
        preFrame.setVisible(true);
		
	}
	
	/*
	 * Main Chat Window
	 */
	public void chatWindow() throws NumberFormatException, UnknownHostException, IOException {
		connection = new Socket(InetAddress.getByName(serverIP), Integer.valueOf(port));
		input = connection.getInputStream();
		output = connection.getOutputStream();
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
		connection = (SSLSocket) new Socket(InetAddress.getByName("127.0.0.1"), 80);
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
				messageBox.setEditable(tof);
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
	
	class enterServerButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			username = usernameBox.getText();
			if (username.length() < 1) {
				System.out.println("Please Enter Username!");
			}
			else {
				preFrame.setVisible(false);
				serverIP = serverIPInput.getText();
				port = portNum.getText();
				try {
					chatWindow();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
