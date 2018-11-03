import java.io.*;
import java.awt.*;
import java.net.*;
import java.security.spec.InvalidKeySpecException;
import java.awt.event.*;
import javax.swing.*;

public class ClientTest
{
	private static String name = "QNTM v0.5";
	private String username = ""; //username is received from prechat login	
	private static JFrame chatFrame = new JFrame(name);
	private static JTextField messageBox; //where the user types
	private static JTextArea chatBox;
	private static JButton sendMessage;
	private static JFrame preFrame;
	private static JTextField usernameBox;
	private static JPasswordField passwordBox;
	private static JTextField serverIPInput;
	private static JTextField portNum;
	private static int serverPort = 0;
	private static String serverIP;
	private static FreshClient client;
	private static boolean isChatting = false;
	
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
			else if (messageBox.getText().equals("!clear")) {
				chatBox.setText("All messages cleared\n");
				messageBox.setText("");
			}
			else {
				// sending message
				try {
					String out = messageBox.getText();
					//byte[] messageOut = AES.encrypt("<" + username + ">"
						//	+ out, dh.getSecret());
					String messageOut = (username + ": " + out);
					client.sendMessageUTF(messageOut);
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
			serverIP = serverIPInput.getText();
            username = usernameBox.getText();
            System.out.println(portNum.getText()); //makre sure this is actually an int before trying
            if(portNum.getText().equals(""))
            	System.out.println("NO");
            else
            	serverPort = Integer.valueOf(portNum.getText());
            if (username.length() < 1) {
                System.out.println("No!");
            } else if(serverPort == 0) {
            	System.out.println("Wrong");
            } else {
                preFrame.setVisible(false);
                client = new FreshClient(serverIP, serverPort, username);
                client.run();
                displayChat();
            }
		}
	};
	
	public void preDisplay() throws NumberFormatException, UnknownHostException, IOException, InvalidKeySpecException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		JTextField serverIP = new JTextField(15);
    	portNum = new JTextField(4);
    	JLabel enterServerIP = new JLabel("Server IP:");
    	JLabel port = new JLabel("Port #:");
    	chatFrame.setVisible(false);
    	portNum.addActionListener(enterServerAction);
    	preFrame = new JFrame(name);
    	usernameBox = new JTextField(15);
    	usernameBox.addActionListener( enterServerAction );
    	passwordBox = new JPasswordField(15);
    	JLabel chooseUsernameLabel = new JLabel("Username:");
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
		Thread listen = new Thread(new Runnable() {
			public void run() {
				String msg = "";
				try {
					while(true)
						display(client.getMessage());
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				display(msg);
			}
		}); listen.start();
	}
	
	public void display(final String message) {
		try {
			 EventQueue.invokeLater(new Runnable() {
				 public void run() {
					 try {
						 chatBox.append(message + "\n");
					 }catch(Exception e) {
						 e.printStackTrace();
					 }
				 }
			 });
		       
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String [] args) throws InterruptedException {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch(Exception e) {
					e.printStackTrace();
				}
				ClientTest mainGUI = new ClientTest();
				try {
					mainGUI.preDisplay();
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}
		
	
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
		} */
