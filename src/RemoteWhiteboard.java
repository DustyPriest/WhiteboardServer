import shapes.ICustomShape;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RemoteWhiteboard extends UnicastRemoteObject implements IRemoteWhiteboard, SubscriberSubject {

    private ConcurrentLinkedQueue<ICustomShape> shapes = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<String> users = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<String> applications = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<String> chatMessages = new ConcurrentLinkedQueue<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private String manager;
    private final ArrayList<Subscriber> subscribers = new ArrayList<>();

    protected RemoteWhiteboard() throws RemoteException {
        super();
    }


    @Override
    public ICustomShape[] getShapes() throws RemoteException {
        return shapes.toArray(new ICustomShape[0]);
    }

    @Override
    public void setShapes(ConcurrentLinkedQueue<ICustomShape> shapes) throws RemoteException {
        this.shapes = shapes;
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
        notifySubscribers("applications", applications.toArray(new String[0]));
    }

    @Override
    public boolean applicationPending(String username) throws RemoteException {
        return applications.contains(username);
    }

    @Override
    public void retractApplication(String username) throws RemoteException {
        applications.remove(username);
        notifySubscribers("applications", applications.toArray(new String[0]));
    }

    public void addUser(String username) {
        users.add(username);
        applications.remove(username);
        try {
            addChatMessage(username + " has joined the whiteboard.", "Server");
            notifySubscribers("chat", chatMessages.toArray(new String[0]));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        notifySubscribers("applications", applications.toArray(new String[0]));
        notifySubscribers("users", users.toArray(new String[0]));
    }

    public void denyUser(String username) {
        applications.remove(username);
        notifySubscribers("applications", applications.toArray(new String[0]));
    }


    @Override
    public void kickUser(String username) throws RemoteException {
        users.remove(username);
        addChatMessage(username + " has departed.", "Server");
        notifySubscribers("chat", chatMessages.toArray(new String[0]));
        notifySubscribers("users", users.toArray(new String[0]));
    }

    @Override
    public String[] getChatMessages() throws RemoteException {
        return chatMessages.toArray(new String[0]);
    }

    @Override
    public void addChatMessage(String message, String user) throws RemoteException {
        chatMessages.add("[" + dateFormat.format(new Date()) + "] " + user + ": " + message);
        notifySubscribers("chat", chatMessages.toArray(new String[0]));
    }


    public void setManager(String username) {
        if (manager == null) {
            manager = username;
            users.add(username);
            notifySubscribers("users", users.toArray(new String[0]));
        } else { // manager leaving whiteboard
            manager = null;
            users.remove(username);
            notifySubscribers("users", users.toArray(new String[0]));
        }
    }

    public String getManager() {
        return manager;
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        if (!subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
        }
    }
    @Override
    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
    @Override
    public void notifySubscribers(String event, String[] data) {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(event, data);
        }
    }
}
