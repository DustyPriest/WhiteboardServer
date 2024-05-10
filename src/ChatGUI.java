import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;

public class ChatGUI extends JFrame implements KeyListener, Subscriber {
    private JPanel mainPanel;
    private JScrollPane chatScrollPane;
    private JTextField messageField;
    private JButton sendButton;
    private JScrollPane activeUsersScrollPane;
    private JTextArea chatArea;
    private JTextArea usersArea;
    private final IRemoteWhiteboard remoteWhiteboard;
    private final String username;

    public ChatGUI(RemoteWhiteboard remoteWhiteboard, String username) {
        super();
        this.remoteWhiteboard = remoteWhiteboard;
        this.username = username;
        remoteWhiteboard.subscribe(this);

        this.setContentPane(mainPanel);
        this.setTitle("Chat & Users");
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(400, 600);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((int) screenSize.getWidth() / 2 + 405, (int) screenSize.getHeight() / 2 - 300, 400, 600);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addKeyListener(this);

        this.setVisible(true);
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            try {
                remoteWhiteboard.addChatMessage(message, username);
                messageField.setText("");
            } catch (RemoteException ex) {
                System.err.println("Failed to send chat message");
                Main.handleConnectionFailure(ex);
            }
        }
    }

    private void updateChatArea(String[] messages) {
        chatArea.setText(String.join("\n", messages));
    }

    private void updateUsersArea(String[] users) {
        usersArea.setText(String.join("\n", users));
    }

    @Override
    public void update(String event, String[] data) {
        switch (event) {
            case "chat":
                updateChatArea(data);
                break;
            case "users":
                updateUsersArea(data);
                break;
            default:
        }
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
        messageField.requestFocus();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            sendMessage();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
