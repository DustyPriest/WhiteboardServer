package shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class CustomEllipse extends Ellipse2D.Double implements ICustomShape {
    private Color color = Color.BLACK;
    private int strokeWidth = 1;
    private final double xOrigin;
    private final double yOrigin;

    public CustomEllipse(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
        this.xOrigin = x1;
        this.yOrigin = y1;
    }

    public CustomEllipse(int x1, int y1, int x2, int y2, Color color, int strokeWidth) {
        this(x1, y1, x2, y2);
        this.color = color;
        this.strokeWidth = strokeWidth;
    }
    @Override
    public void updateBounds(int x, int y) {
        updateBounds(x, (double) y);
    }

    public void updateBounds(double x, double y) {
        if (x < xOrigin) {
            this.width = xOrigin - x;
            this.x = x;
        } else {
            this.width = x - xOrigin;
        }
        if (y < yOrigin) {
            this.height = yOrigin - y;
            this.y = y;
        } else {
            this.height = y - yOrigin;
        }
    }


    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(strokeWidth));
        g2.draw(this);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public int getStrokeWidth() {
        return strokeWidth;
    }


}
