import shapes.ICustomShape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteWhiteboard extends Remote {

    ArrayList<ICustomShape> getShapes() throws RemoteException;
    void addShape(ICustomShape shape) throws RemoteException;
    void clearShapes() throws RemoteException;

    ArrayList<String> getCurrentUsers() throws RemoteException;
    void addUser(String username) throws RemoteException;
    void kickUser(String username) throws RemoteException;
    ArrayList<String> getChatMessages() throws RemoteException;
    void addChatMessage(String message, String user) throws RemoteException;
    boolean validateUsername(String username) throws RemoteException;

}
