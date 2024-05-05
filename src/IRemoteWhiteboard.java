import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteWhiteboard extends Remote {

    public ArrayList<Shape> getShapes() throws RemoteException;
    public void addShape(Shape shape) throws RemoteException;
    public void clearShapes() throws RemoteException;

}
