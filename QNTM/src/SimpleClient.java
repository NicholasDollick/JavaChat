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


public class SimpleClient {
	static String name = "QNTM v0.3";
	private static Socket connection;
	private String username = "Client"; //username is received from prechat login	
	static JFrame chatFrame = new JFrame(name);
	static JTextField messageBox; //where the user types
	static JTextArea chatBox;
	static JButton sendMessage;
	static JFrame preFrame;
	DH client = new DH(); // init DH algo
	static  JTextField usernameBox;
	static  JPasswordField passwordBox;
	static JTextField serverIPInput;
	static JTextField portNum;
	
	static DataInputStream din;
	static DataOutputStream dout;

	public static void main(String[] args) {
		try {
			connection = new Socket(InetAddress.getByName("localhost"), 2423);
			din = new DataInputStream(connection.getInputStream());
			dout = new DataOutputStream(connection.getOutputStream());
			SimpleClient test = new SimpleClient();
			String messageIn = "", messageOut = "";
			
			while(!messageIn.equals("CLOSE")) {
				messageIn = din.readUTF();
				chatBox.append(messageIn);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Main Chat Window
	 */
	public SimpleClient() throws NumberFormatException, UnknownHostException, IOException, InvalidKeySpecException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
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
	
	
}
