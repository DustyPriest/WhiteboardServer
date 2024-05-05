package shapes;

import java.awt.*;

public class CustomRectangle extends Rectangle implements ICustomShape {

    private Color color = Color.BLACK;
    private int strokeWidth = 1;
    private final int xOrigin;
    private final int yOrigin;
    public CustomRectangle(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.xOrigin = x;
        this.yOrigin = y;
    }

    public CustomRectangle(int x, int y, int width, int height, Color color, int strokeWidth) {
        this(x, y, width, height);
        this.color = color;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void updateBounds(int x, int y2) {
        if (x < xOrigin) {
            this.width = xOrigin - x;
            this.x = x;
        } else {
            this.width = x - xOrigin;
        }
        if (y2 < yOrigin) {
            this.height = yOrigin - y2;
            this.y = y2;
        } else {
            this.height = y2 - yOrigin;
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
