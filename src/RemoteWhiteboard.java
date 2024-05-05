import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class RemoteWhiteboard implements IRemoteWhiteboard {

    private ArrayList<Shape> shapes = new ArrayList<Shape>();


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
