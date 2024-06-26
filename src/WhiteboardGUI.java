import shapes.ICustomShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentLinkedQueue;


public class WhiteboardGUI extends JFrame {
    private JPanel mainPanel;
    private JToolBar drawingOptionsToolbar;
    private JButton brushButton;
    private JButton eraseButton;
    private JButton lineButton;
    private JButton rectangleButton;
    private JButton circleButton;
    private JButton ovalButton;
    private JButton textButton;
    private JButton chatButton;
    private JToolBar styleOptionsToolbar;
    private JButton colorButton;
    private JSlider sizeSlider;
    private JSpinner fontSizeSpinner;
    private JComboBox<String> fontComboBox;
    private JCheckBox fillCheckBox;
    private final WhiteboardCanvas whiteboardCanvas;
    private JButton selectedDrawingButton = brushButton;
    private final ChatGUI chatGUI;
    private final IRemoteWhiteboard remoteWhiteboard;
    private boolean saved = false;
    private File file;

    public WhiteboardGUI(RemoteWhiteboard remoteWhiteboard, String username) {
        super();
        this.remoteWhiteboard = remoteWhiteboard;
        remoteWhiteboard.setManager(username);

        whiteboardCanvas = new WhiteboardCanvas(remoteWhiteboard);
        mainPanel.add(whiteboardCanvas, BorderLayout.CENTER);
        for (String name : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            fontComboBox.addItem(name);
        }
        fontComboBox.setSelectedItem("Arial");
        fontSizeSpinner.setValue(12);
        fontSizeSpinner.setModel(new SpinnerNumberModel(12, 1, 72, 1));

        addChatButton();
        setDrawingOptionsListeners();
        setStyleOptionsListeners();
        setupMenuBar();

        setDrawingMode(DrawingMode.BRUSH, brushButton);
        this.setTitle("Whiteboard");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                remoteWhiteboard.setManager(username);
                chatGUI.dispose();
            }
        });
        this.setSize(800, 600);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((int) screenSize.getWidth() / 2 - 400, (int) screenSize.getHeight() / 2 - 300, 800, 600);
        this.setVisible(true);

        chatGUI = new ChatGUI(remoteWhiteboard, username);
        this.requestFocus();
    }

    public void close() {
        this.dispose();
        chatGUI.dispose();
    }

    private void setDrawingOptionsListeners() {
        brushButton.addActionListener(e -> setDrawingMode(DrawingMode.BRUSH, brushButton));
        eraseButton.addActionListener(e -> setDrawingMode(DrawingMode.ERASE, eraseButton));
        lineButton.addActionListener(e -> setDrawingMode(DrawingMode.LINE, lineButton));
        rectangleButton.addActionListener(e -> setDrawingMode(DrawingMode.RECTANGLE, rectangleButton));
        circleButton.addActionListener(e -> setDrawingMode(DrawingMode.CIRCLE, circleButton));
        ovalButton.addActionListener(e -> setDrawingMode(DrawingMode.OVAL, ovalButton));
        textButton.addActionListener(e -> setDrawingMode(DrawingMode.TEXT, textButton));
        fillCheckBox.addActionListener(e -> whiteboardCanvas.setFillSelected(fillCheckBox.isSelected()));
        chatButton.addActionListener(e -> {
            chatGUI.setVisible(true);
            chatGUI.requestFocus();
        });
    }

    private void setStyleOptionsListeners() {
        sizeSlider.addChangeListener(e -> whiteboardCanvas.setDrawingStroke(sizeSlider.getValue()));
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", whiteboardCanvas.getDrawingColor());
            if (newColor != null) {
                whiteboardCanvas.setDrawingColor(newColor);
            }
        });
        fontSizeSpinner.addChangeListener(e -> {
            int newSize = (int) fontSizeSpinner.getValue();
            whiteboardCanvas.setFontSize(newSize);
        });
        fontComboBox.addActionListener(e -> {
            String fontName = (String) fontComboBox.getSelectedItem();
            whiteboardCanvas.setFontFamily(fontName);
        });
    }

    private void setDrawingMode(DrawingMode newMode, JButton button) {
        selectedDrawingButton.setEnabled(true);
        selectedDrawingButton = button;
        selectedDrawingButton.setEnabled(false);
        whiteboardCanvas.setDrawingMode(newMode);
    }

    private void addChatButton() {
        chatButton = new JButton("Chat");
        chatButton.setToolTipText("Show Chat");
        drawingOptionsToolbar.addSeparator();
        drawingOptionsToolbar.add(Box.createGlue());
        drawingOptionsToolbar.add(chatButton);
        drawingOptionsToolbar.addSeparator();
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem saveAsMenuItem = new JMenuItem("Save As");

        newMenuItem.addActionListener(e -> newFile());
        openMenuItem.addActionListener(e -> open());
        saveMenuItem.addActionListener(e -> {
            try {
                this.file = WhiteboardIOUtils.save(remoteWhiteboard.getShapes(), file);
            } catch (RemoteException ex) {
                System.err.println("Failed to retrieve shapes to save");
                Main.handleConnectionFailure(ex);
            }
        });
        saveAsMenuItem.addActionListener(e -> {
            try {
                this.file = WhiteboardIOUtils.saveAs(remoteWhiteboard.getShapes());
            } catch (RemoteException ex) {
                System.err.println("Failed to retrieve shapes to save");
                Main.handleConnectionFailure(ex);
            }
        });

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
    }

    private void open() {
        File file = WhiteboardIOUtils.selectFile("Open","Open Whiteboard");
        if (file != null) {
            ConcurrentLinkedQueue<ICustomShape> shapes = WhiteboardIOUtils.loadShapes(file);
            ((RemoteWhiteboard) remoteWhiteboard).setShapes(shapes);
            this.file = file;
            whiteboardCanvas.repaint();
        }
    }

    private void newFile() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to start a new whiteboard?", "New Whiteboard", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            ((RemoteWhiteboard) remoteWhiteboard).clearShapes();
            file = null;
            whiteboardCanvas.repaint();
        }
    }

}
