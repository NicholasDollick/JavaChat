import java.io.*;
import java.awt.*;
import java.net.*;
import java.security.spec.InvalidKeySpecException;
import java.awt.event.*;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.*;

public class ClientTest
{
	private static String name = "QNTM v0.4";
	private String username = "Client"; //username is received from prechat login	
	private static JFrame chatFrame = new JFrame(name);
	private static JTextField messageBox; //where the user types
	private static JTextArea chatBox;
	private static JButton sendMessage;
	private static JFrame preFrame;
	private static JTextField usernameBox;
	private static JPasswordField passwordBox;
	private static JTextField serverIPInput;
	private static JTextField portNum;
	private static DataInputStream inputStream;
	private static DataOutputStream outputStream;
	private static DH dh = new DH();
	
	// generic listener for both send button and enter key
	Action sendMessageAction = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (messageBox.getText().length() < 1) {
				//empty message, do nothing
			}
			else if (messageBox.getText().equals(".clear")) {
				chatBox.setText("All messages cleared\n");
				messageBox.setText("");
			}
			else {
				// sending message
				try {
					String out = messageBox.getText();
					//byte[] messageOut = AES.encrypt("<" + username + ">"
						//	+ out, dh.getSecret());
					String messageOut = ("<" + username + ">" + out);
					outputStream.writeUTF(messageOut);
					//chatBox.append("<" + username + ">" + out + "\n");

				} catch (Exception e2) {
					e2.printStackTrace();
				}
				messageBox.setText("");
			}
			messageBox.requestFocusInWindow();
		}
	};
	
	// generic action for joining server with button or enter key
	Action enterServerAction = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2L;

		@Override
		public void actionPerformed(ActionEvent e) {
            username = usernameBox.getText();
            if (username.length() < 1) {
                System.out.println("No!");
            } else {
                preFrame.setVisible(false);
                displayChat();
            }
		}
	};
	
	public ClientTest() throws NumberFormatException, UnknownHostException, IOException, InvalidKeySpecException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		JTextField serverIP = new JTextField(15);
    	JTextField portNum = new JTextField(4);
    	JLabel enterServerIP = new JLabel("Server IP:");
    	JLabel port = new JLabel("Port #:");
    	chatFrame.setVisible(false);
    	preFrame = new JFrame(name);
    	usernameBox = new JTextField(15);
    	usernameBox.addActionListener( enterServerAction );
    	passwordBox = new JPasswordField(15);
    	JLabel chooseUsernameLabel = new JLabel("username:");
    	JLabel choosePassword = new JLabel("password:");
    	JButton enterServer = new JButton("Enter Chat Server");
    	JButton createAccount = new JButton("Create Account");
    	enterServer.addActionListener( enterServerAction );
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
        //prePanel.add(enterServerIP, preLeft);
        //prePanel.add(serverIP, preRight);
        //prePanel.add(port, preLeft);
        //prePanel.add(portNum, preRight);
        preFrame.add(prePanel, BorderLayout.CENTER);
        preFrame.add(createAccount, BorderLayout.SOUTH);
        preFrame.add(enterServer, BorderLayout.SOUTH);
        preFrame.setSize(350, 350);
        preFrame.setVisible(true);
	}

	public void displayChat() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		JPanel southPanel = new JPanel();
		southPanel.setBackground(Color.BLUE);
		southPanel.setLayout(new GridBagLayout());
		
		messageBox = new JTextField(30);
		messageBox.requestFocusInWindow();
		messageBox.addActionListener( sendMessageAction );
		
		sendMessage = new JButton("Send Message");
		sendMessage.addActionListener( sendMessageAction );
		
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
	
	
	public static void main(String [] args) throws InterruptedException {
		int port = 2423;
		
		//configure ssl
		System.setProperty("javax.net.ssl.trustStore", "myTrustStore.jts");
		System.setProperty("javax.net.ssl.trustStorePassword", "asdf123");
		//System.setProperty("javax.net.debug", "ssl");
		try {
			SSLSocketFactory sockFact = (SSLSocketFactory)SSLSocketFactory.getDefault();
			SSLSocket sslSocket = (SSLSocket)sockFact.createSocket("localhost",port); //something about inet addy might be neded
			outputStream = new DataOutputStream(sslSocket.getOutputStream());
			inputStream = new DataInputStream(sslSocket.getInputStream());
			
			/*
			// DH key exchange for message encryption
			try {
				//generate keys
				System.out.println("{*} Starting key exchange");
				dh.generateKeys();
				System.out.println("{*} Waiting for Key");
				int len = inputStream.readInt();
				if(len > 0) {
					byte[] m = new byte[len];
					inputStream.readFully(m, 0, m.length);
					dh.receivePublicKey(m);
				}
				System.out.println("{+} Key Recieved");
				
				
				
				System.out.println("{*} Sending Key");
				byte[] pubKey = dh.getPublicKeyEnc();
				len = pubKey.length;
				outputStream.writeInt(len);
				outputStream.write(pubKey);
				System.out.println("{+} Key Sent");
				
				dh.getShared();
				System.out.println(dh.getSecret());
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			System.out.println();
			System.out.println("{+} Communication now encrypted");
			System.out.println();
			*/
			
			// generate instance of client UI
			ClientTest test = new ClientTest();
			
			while(true) {
				String message = "";
				
				message = inputStream.readUTF();
				
				chatBox.append(message + "\n");
							
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}