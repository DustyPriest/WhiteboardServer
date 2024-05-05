package shapes;

import java.awt.*;
import java.util.ArrayList;

public class CustomBrush implements ICustomShape {

    private ArrayList<Point> points = new ArrayList<>();
    private Color color = Color.BLACK;
    private int strokeWidth = 1;

    public CustomBrush(int x, int y, Color color, int strokeWidth) {
        points.add(new Point(x, y));
        this.color = color;
        this.strokeWidth = strokeWidth;
    }
    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public int getStrokeWidth() {
        return strokeWidth;
    }

    @Override
    public void updateBounds(int x, int y) {
        points.add(new Point(x, y));
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 1; i < points.size(); i++) {
            Point p1 = points.get(i - 1);
            Point p2 = points.get(i);
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }
}
