import shapes.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

enum DrawingMode {
    BRUSH,
    ERASE,
    LINE,
    RECTANGLE,
    CIRCLE,
    OVAL,
    TEXT
}
public class WhiteboardCanvas extends JPanel implements MouseInputListener, KeyListener, Subscriber {

    private boolean fillSelected = false;
    private boolean enteringText = false;
    private DrawingMode drawingMode = DrawingMode.BRUSH;
    private Font font = new Font("Arial", Font.PLAIN, 12);
    private Color canvasColor = Color.WHITE;

    private Color drawingColor = Color.BLACK;
    private int drawingStroke = 1;
    private ICustomShape[] shapes;
    private ICustomShape previewShape;
    private final RemoteWhiteboard remoteWhiteboardState;

    public WhiteboardCanvas(RemoteWhiteboard remoteWhiteboardState) {
        super();
        this.remoteWhiteboardState = remoteWhiteboardState;
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        remoteWhiteboardState.subscribe(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        shapes = remoteWhiteboardState.getShapes();
        for (ICustomShape shape : shapes) {
            shape.draw(g2);
        }
        if (previewShape != null) {
            previewShape.draw(g2);
        }
    }

    protected void setDrawingColor(Color color) {
        this.drawingColor = color;
    }

    protected void setDrawingStroke(int width) {
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.setStroke(new BasicStroke(width));
        drawingStroke = width;
    }


    protected void setDrawingMode(DrawingMode drawingMode) {
        if (drawingMode == DrawingMode.ERASE) {
            setDrawingColor(canvasColor);
        } else if (this.drawingMode == DrawingMode.ERASE) {
            setDrawingColor(Color.BLACK);
        }
        this.drawingMode = drawingMode;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow();
        switch (drawingMode) {
            case ERASE:
                remoteWhiteboardState.addShape(new CustomBrush(e.getX(), e.getY(), canvasColor, drawingStroke));
                repaint();
                break;
            case BRUSH:
                remoteWhiteboardState.addShape(new CustomBrush(e.getX(), e.getY(), drawingColor, drawingStroke));
                repaint();
                break;
            case TEXT:
                if (enteringText) {
                    finaliseText((CustomText) previewShape);
                } else {
                    enteringText = true;
                    previewShape = new CustomText("", e.getX(), e.getY(), drawingColor, font);
                }
                repaint();
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (drawingMode) {
            case ERASE, BRUSH, LINE, RECTANGLE, OVAL:
                previewShape.updateBounds(e.getX(), e.getY());
                repaint();
                break;
            case CIRCLE:
                CustomEllipse currPreview = (CustomEllipse) previewShape;
                double diameter = e.getX() - currPreview.getX();
                currPreview.updateBounds(currPreview.getX() + diameter, currPreview.getY() + diameter);
                repaint();
                break;
            default:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocusInWindow();
        if (!enteringText) {
            switch (drawingMode) {
                case ERASE:
                    previewShape = new CustomBrush(e.getX(), e.getY(), canvasColor, drawingStroke);
                    repaint();
                    break;
                case BRUSH:
                    previewShape = new CustomBrush(e.getX(), e.getY(), drawingColor, drawingStroke);
                    repaint();
                    break;
                case LINE:
                    previewShape = new CustomLine(e.getX(), e.getY(), e.getX(), e.getY(), drawingColor, drawingStroke);
                    break;
                case RECTANGLE:
                    previewShape = new CustomRectangle(e.getX(), e.getY(), 1, 1, drawingColor, drawingStroke, fillSelected);
                    break;
                case CIRCLE, OVAL:
                    previewShape = new CustomEllipse(e.getX(), e.getY(), 1, 1, drawingColor, drawingStroke, fillSelected);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (previewShape != null && !enteringText) {
            remoteWhiteboardState.addShape(previewShape);
            previewShape = null;
            this.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (enteringText) {
            CustomText currPreview = (CustomText) previewShape;
            char c = e.getKeyChar();
            if (c == KeyEvent.VK_BACK_SPACE) {
                currPreview.backspace();
            } else if (c == KeyEvent.VK_ENTER) {
                finaliseText(currPreview);
            } else if (c == KeyEvent.VK_ESCAPE) {
                enteringText = false;
                previewShape = null;
            } else {
                currPreview.append(e.getKeyChar());
            }
            repaint();
        }
    }

    private void finaliseText(CustomText customText) {
        enteringText = false;
        if (!customText.isEmpty()) {
            customText.toggleCaret();
            remoteWhiteboardState.addShape(customText);
        }
        previewShape = null;
    }

    public Color getDrawingColor() {
        return drawingColor;
    }

    public void setFontSize(int size) {
        font = new Font(font.getName(), font.getStyle(), size);
    }

    public void setFontFamily(String family) {
        font = new Font(family, font.getStyle(), font.getSize());
    }

    public void setFillSelected(boolean selection) {
        fillSelected = selection;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void update(String event, String[] data) {
        if (event.equals("shape")) {
            repaint();
        }
    }
}
