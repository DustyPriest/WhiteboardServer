import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RemoteWhiteboard extends UnicastRemoteObject implements IRemoteWhiteboard {

    private ArrayList<Shape> shapes = new ArrayList<Shape>();

    protected RemoteWhiteboard() throws RemoteException {
        super();
    }


    @Override
    public ArrayList<Shape> getShapes() throws RemoteException {
        return shapes;
    }

    @Override
    public void addShape(Shape shape) throws RemoteException {
        shapes.add(shape);
    }

    @Override
    public void clearShapes() throws RemoteException {
        shapes.clear();
    }
}
