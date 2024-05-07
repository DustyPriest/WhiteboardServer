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
    private ArrayList<String> chatMessages = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

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
    public void addUser(String username) throws RemoteException {
        if (!users.contains(username)) {
            users.add(username);
            addChatMessage(username + " has joined the whiteboard.", "Server");
        }
    }

    @Override
    public void kickUser(String username) throws RemoteException {
        users.remove(username);
        addChatMessage(username + " has departed.", "Server");
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
}
