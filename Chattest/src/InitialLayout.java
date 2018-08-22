import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// clients should generate secret key passed on from server upon session creation
// needs to be refactored to not have a main in here
//
// add db support to this local test before moving forward.
// needed: register button(with own window)

public class InitialLayout{
    String name = "Chatty E2E v0.1";
    InitialLayout mainGUI;
    JFrame newFrame = new JFrame(name);
    JButton sendMessage;
    JTextField messageBox;
    JTextArea chatBox;
    JTextField usernameBox;
    JPasswordField passwordBox;
    JFrame preFrame;
    String username; //will later be replaced via a cmd fetch

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable () {
            public void run() {
                try{
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                InitialLayout mainGUI = new InitialLayout();
                mainGUI.preDisplay();
            }
        });
    }
    
    public void preDisplay() {
    	newFrame.setVisible(false);
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
        prePanel.add(choosePassword, preLeft);
        prePanel.add(passwordBox, preRight);
        preFrame.add(prePanel, BorderLayout.CENTER);
        preFrame.add(createAccount, BorderLayout.SOUTH);
        preFrame.add(enterServer, BorderLayout.SOUTH);
        preFrame.setSize(350, 350);
        preFrame.setVisible(true);
    }

    public void display() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener(new sendMessageButtonListener());

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

        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(470, 300);
        newFrame.setVisible(true);
    }
    
    // this should encrypt and then send message off to server
    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (messageBox.getText().length() < 1) {
                // do nothing
            } else if (messageBox.getText().equals(".clear")) {
                chatBox.setText("Cleared all messages\n");
                messageBox.setText("");
            } else {
                chatBox.append("<" + username + ">:  " + messageBox.getText()
                        + "\n");
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
                display();
            }
        }

    }

}