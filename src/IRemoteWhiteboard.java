import shapes.ICustomShape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface IRemoteWhiteboard extends Remote {

    ICustomShape[] getShapes() throws RemoteException;
    void setShapes(ConcurrentLinkedQueue<ICustomShape> shapes) throws RemoteException;
    void addShape(ICustomShape shape) throws RemoteException;
    void clearShapes() throws RemoteException;

    String[] getCurrentUsers() throws RemoteException;
    boolean userExists(String username) throws RemoteException;
    void applyForConnection(String username) throws RemoteException;
    boolean applicationPending(String username) throws RemoteException;
    void retractApplication(String username) throws RemoteException;
    void kickUser(String username) throws RemoteException;
    String[] getChatMessages() throws RemoteException;
    void addChatMessage(String message, String user) throws RemoteException;

}
