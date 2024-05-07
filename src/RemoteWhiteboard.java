import shapes.ICustomShape;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RemoteWhiteboard extends UnicastRemoteObject implements IRemoteWhiteboard {

    private ConcurrentLinkedQueue<ICustomShape> shapes = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<String> users = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<String> applications = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<String> chatMessages = new ConcurrentLinkedQueue<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private ServerGUI serverGUI;
    private String manager;

    protected RemoteWhiteboard() throws RemoteException {
        super();
    }


    @Override
    public ICustomShape[] getShapes() throws RemoteException {
        return shapes.toArray(new ICustomShape[0]);
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
    public String[] getCurrentUsers() throws RemoteException {
        return users.toArray(new String[0]);
    }

    @Override
    public boolean userExists(String username) throws RemoteException {
        return users.contains(username);
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

    @Override
    public void retractApplication(String username) throws RemoteException {
        applications.remove(username);
        serverGUI.updateApplicationList(applications.toArray(new String[0]));
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

    public void denyUser(String username) {
        applications.remove(username);
        serverGUI.updateApplicationList(applications.toArray(new String[0]));
    }


    @Override
    public void kickUser(String username) throws RemoteException {
        users.remove(username);
        addChatMessage(username + " has departed.", "Server");
        serverGUI.updateUserList(users.toArray(new String[0]));
    }

    @Override
    public String[] getChatMessages() throws RemoteException {
        return chatMessages.toArray(new String[0]);
    }

    @Override
    public void addChatMessage(String message, String user) throws RemoteException {
        chatMessages.add("[" + dateFormat.format(new Date()) + "] " + user + ": " + message);
    }


    public void setServerGUI(ServerGUI serverGUI) {
        if (this.serverGUI == null) {
            this.serverGUI = serverGUI;
        }
    }

    public void addManager(String username) {
        if (manager == null) {
            manager = username;
            users.add(username);
            serverGUI.updateUserList(users.toArray(new String[0]));
        }
    }

    public String getManager() {
        return manager;
    }
}
