import shapes.ICustomShape;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WhiteboardIOUtils {

    public static void saveShapes(ICustomShape[] shapes, File file) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(file);
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(shapes);
        objOut.close();
        fileOut.close();
    }

    public static ConcurrentLinkedQueue<ICustomShape> loadShapes(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream objIn = new ObjectInputStream(fileIn);
        ICustomShape[] shapesArray = (ICustomShape[]) objIn.readObject();
        objIn.close();
        fileIn.close();
        return new ConcurrentLinkedQueue<>(Arrays.asList(shapesArray));
    }


}
