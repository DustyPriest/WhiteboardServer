import shapes.ICustomShape;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RemoteWhiteboard extends UnicastRemoteObject implements IRemoteWhiteboard {

    private ArrayList<ICustomShape> shapes = new ArrayList<>();
    private ArrayList<String> users = new ArrayList<>();
    private ArrayList<String> applications = new ArrayList<>();
    private ArrayList<String> chatMessages = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private ServerGUI serverGUI;

    protected RemoteWhiteboard() throws RemoteException {
        super();
    }


    @Override
    public ArrayList<ICustomShape> getShapes() throws RemoteException {
        return shapes;
    }

    @Override
    public void addShape(ICustomShape shape) throws RemoteException {
        shapes.add(shape);
    }

    @Override
    public void clearShapes() throws RemoteException {
        shapes.clear();
    }

    @Override
    public ArrayList<String> getCurrentUsers() throws RemoteException {
        return users;
    }

    @Override
    public void applyForConnection(String username) throws RemoteException {
        applications.add(username);
        serverGUI.updateApplicationList(applications.toArray(new String[0]));
    }

    @Override
    public boolean applicationPending(String username) throws RemoteException {
        return applications.contains(username);
    }

    public void addUser(String username) {
        users.add(username);
        applications.remove(username);
        try {
            addChatMessage(username + " has joined the whiteboard.", "Server");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        serverGUI.updateApplicationList(applications.toArray(new String[0]));
        serverGUI.updateUserList(users.toArray(new String[0]));
    }


    @Override
    public void kickUser(String username) throws RemoteException {
        users.remove(username);
        addChatMessage(username + " has departed.", "Server");
        serverGUI.updateUserList(users.toArray(new String[0]));
    }

    @Override
    public ArrayList<String> getChatMessages() throws RemoteException {
        return chatMessages;
    }

    @Override
    public void addChatMessage(String message, String user) throws RemoteException {
        chatMessages.add("[" + dateFormat.format(new Date()) + "] " + user + ": " + message);
    }

    @Override
    public boolean validateUsername(String username) throws RemoteException {
        return !users.contains(username);
    }

    public void setServerGUI(ServerGUI serverGUI) {
        if (this.serverGUI == null) {
            this.serverGUI = serverGUI;
        }
    }
}
