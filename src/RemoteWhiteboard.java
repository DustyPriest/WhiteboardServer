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
    public ICustomShape[] getShapes() {
        return shapes.toArray(new ICustomShape[0]);
    }

    public void setShapes(ConcurrentLinkedQueue<ICustomShape> shapes) {
        this.shapes = shapes;
    }

    @Override
    public void addShape(ICustomShape shape) {
        shapes.add(shape);
        notifySubscribers("shape", null);
    }

    public void clearShapes() {
        shapes.clear();
    }

    @Override
    public String[] getCurrentUsers() {
        return users.toArray(new String[0]);
    }

    @Override
    public boolean userExists(String username) {
        return users.contains(username);
    }

    @Override
    public void applyForConnection(String username) {
        applications.add(username);
        notifySubscribers("applications", applications.toArray(new String[0]));
    }

    @Override
    public boolean applicationPending(String username) {
        return applications.contains(username);
    }

    @Override
    public void retractApplication(String username) {
        applications.remove(username);
        notifySubscribers("applications", applications.toArray(new String[0]));
    }

    public void addUser(String username) {
        users.add(username);
        applications.remove(username);
        addChatMessage(username + " has joined the whiteboard.", "Server");
        notifySubscribers("chat", chatMessages.toArray(new String[0]));
        notifySubscribers("applications", applications.toArray(new String[0]));
        notifySubscribers("users", users.toArray(new String[0]));
    }

    public void denyUser(String username) {
        applications.remove(username);
        notifySubscribers("applications", applications.toArray(new String[0]));
    }


    @Override
    public void kickUser(String username) {
        users.remove(username);
        addChatMessage(username + " has departed.", "Server");
        notifySubscribers("chat", chatMessages.toArray(new String[0]));
        notifySubscribers("users", users.toArray(new String[0]));
    }

    @Override
    public String[] getChatMessages() {
        return chatMessages.toArray(new String[0]);
    }

    @Override
    public void addChatMessage(String message, String user) {
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
