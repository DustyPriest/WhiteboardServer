import shapes.ICustomShape;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.rmi.RemoteException;
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

    public static ConcurrentLinkedQueue<ICustomShape> loadShapes(File file) {
        ICustomShape[] shapesArray;
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            shapesArray = (ICustomShape[]) objIn.readObject();
            objIn.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Failed to load file.", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Failed to load file");
            e.printStackTrace();
            return new ConcurrentLinkedQueue<>();
        }
        return new ConcurrentLinkedQueue<>(Arrays.asList(shapesArray));
    }

    public static File selectFile(String type, String title) {
        String suffix = ".ser";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Whiteboard Files", "ser"));
        fileChooser.setDialogTitle(title);
        int response;
        if (type.equals("Open")) {
            response = fileChooser.showOpenDialog(null);
        } else {
            response = fileChooser.showSaveDialog(null);
        }
        if (response == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(suffix)) {
                file = new File(file.getAbsolutePath() + suffix);
            }
            return file;
        }
        return null;
    }

    public static File saveAs(ICustomShape[] shapes) {
        File file = WhiteboardIOUtils.selectFile("Save", "Save Whiteboard As");
        if (file != null) {
            try {
                WhiteboardIOUtils.saveShapes(shapes, file);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to save file.", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Failed to save file");
                e.printStackTrace();
            }
        }
        return file;
    }

    public static File save(ICustomShape[] shapes, File file) {
        if (file == null) {
            return WhiteboardIOUtils.saveAs(shapes);
        } else {
            try {
                WhiteboardIOUtils.saveShapes(shapes, file);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to save file.", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Failed to save file");
                e.printStackTrace();
            }
        }
        return file;
    }


}
