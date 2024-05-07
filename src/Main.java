import javax.swing.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    private static int port;
    private static String username;


    public static void main(String[] args) {

        if (!parseArgs(args)) {
            System.out.println("Usage: java -jar WhiteboardServer.jar <port> <username>");
            return;
        }

        RemoteWhiteboard remoteWhiteboard;
        try {

            LocateRegistry.createRegistry(port);
            remoteWhiteboard = new RemoteWhiteboard();
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("RemoteWhiteboard", remoteWhiteboard);
            new ServerGUI(remoteWhiteboard);

        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "Failed to start server.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(0);
        } catch (AlreadyBoundException e) {
            JOptionPane.showMessageDialog(null, "Failed to bind server to registry (already bound).", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(0);
        }

    }

    private static boolean parseArgs(String[] args) {
        if (args.length != 2) {
            return false;
        }
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return false;
        }
        username = args[1];
        return true;
    }
}
