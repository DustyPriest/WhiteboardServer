package shapes;

import java.awt.*;
import java.awt.geom.Line2D;

public class CustomLine extends Line2D.Double implements ICustomShape {

    private Color color = Color.BLACK;
    private int strokeWidth = 1;

    public CustomLine(int x1, int y1, int x2, int y2, Color color, int strokeWidth) {
        super(x1, y1, x2, y2);
        this.color = color;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void updateBounds(int x, int y) {
        setLine(this.x1, this.y1, x, y);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
