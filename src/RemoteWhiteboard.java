import shapes.ICustomShape;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RemoteWhiteboard extends UnicastRemoteObject implements IRemoteWhiteboard {

    private ArrayList<ICustomShape> shapes = new ArrayList<>();

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
}
