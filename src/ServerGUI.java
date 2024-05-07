import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;

public class ServerGUI extends JFrame {
    private JPanel mainPanel;
    private JList<String> userList;
    private JList<String> applicationList;
    private JButton kickButton;
    private JButton acceptUserButton;
    private final RemoteWhiteboard whiteboardState;
    private final Timer userUpdateTimer;

    public ServerGUI(RemoteWhiteboard whiteboardState) {
        super();

        userList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        applicationList.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        WindowListener closeListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to close the whiteboard?" +
                        "\nThis will kick all active users.", "Close Whiteboard"
                        , JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        };

        acceptUserButton.addActionListener(e -> {
            String selectedUser = applicationList.getSelectedValue();
            if (selectedUser != null) {
                whiteboardState.addUser(selectedUser);
            }
        });

        kickButton.addActionListener(e -> {
            String selectedUser = userList.getSelectedValue();
            if (selectedUser != null) {
                try {
                    whiteboardState.kickUser(selectedUser);
                } catch (RemoteException ex) {
                    System.err.println("Failed to kick user: " + selectedUser);
                    ex.printStackTrace();
                }
            }
        });

        userUpdateTimer = new Timer(1000, e -> {
            try {
                userList.setListData(whiteboardState.getCurrentUsers().toArray(new String[0]));
                applicationList.setListData(whiteboardState.getApplications().toArray(new String[0]));
            } catch (RemoteException ex) {
                System.err.println("Failed to get users or applications");
                ex.printStackTrace();
            }
        });
        userUpdateTimer.start();

        this.whiteboardState = whiteboardState;
        this.setContentPane(mainPanel);
        this.setTitle("Whiteboard Server");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(closeListener);
        this.setSize(400, 400);
        this.setVisible(true);
    }
}
