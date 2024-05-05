import shapes.ICustomShape;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteWhiteboard extends Remote {

    public ArrayList<ICustomShape> getShapes() throws RemoteException;
    public void addShape(ICustomShape shape) throws RemoteException;
    public void clearShapes() throws RemoteException;

}
