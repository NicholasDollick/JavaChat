import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.swing.*;


public class SimpleServer
{
	static String name = "QNTM v0.3";
	private String username = "Server"; //username is received from prechat login
	static JFrame chatFrame = new JFrame(name);
	static JTextField messageBox; //where the user types
	static JTextArea chatBox;
	static JButton sendMessage;
	static JFrame preFrame;
	DH client = new DH(); // init DH algo
	static JTextField usernameBox;
    static JPasswordField passwordBox;
    static JTextField serverIPInput;
	static JTextField portNum;
	private static ServerSocket server;
	private static Socket connection;
	static DataInputStream din;
	static DataOutputStream dout;
	
	//constructor
	public static void main(String [] args) throws InterruptedException {
		int port = 2423;
		System.out.println("[+] Starting Server");
		try {
			server = new ServerSocket(port);
			System.out.println("Waiting for client...");			
			connection = server.accept();
			System.out.println("Accepted connection from: " + connection);
			SimpleServer serv = new SimpleServer();
			din = new DataInputStream(connection.getInputStream());
			dout = new DataOutputStream(connection.getOutputStream());
			
			String messageIn = "", messageOut = "";
			
			while(!messageIn.equals("CLOSE")) {
				messageIn = din.readUTF();
				chatBox.append(messageIn);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public void display() {
    	JTextField serverIP = new JTextField(15);
    	JTextField portNum = new JTextField(4);
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
    	enterServer.addActionListener(
    			new enterServerButtonListener());
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
        prePanel.add(serverIP, preRight);
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
	public void SimpleServer() throws NumberFormatException, UnknownHostException, IOException {
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
					dout.writeUTF("<" + username + ">" + messageBox.getText()
							+ "\n");
					dout.flush();
					
					chatBox.append("<" + username + ">" + messageBox.getText()
							+ "\n");
				} catch (Exception e) {
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
	                System.out.println("No!");
	            } else {
	                preFrame.setVisible(false);
	                try {
						display();
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	            }
	        }
	}

}