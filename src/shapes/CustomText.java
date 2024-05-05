package shapes;

import java.awt.*;

public class CustomText implements ICustomShape {

    private Color color = Color.BLACK;
    private Font font = new Font("Arial", Font.PLAIN, 12);
    private int charHeight = 0;
    private String text;
    private final int x;
    private final int y;
    private boolean showCaret = true;

    public CustomText(String text, int x, int y, Color color, Font font) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
        this.font = font;
    }


    @Override
    public void draw(Graphics2D g2) {
        if (charHeight == 0) {
            charHeight = g2.getFontMetrics(font).getHeight();
        }
        if (!text.isEmpty()) {
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setFont(font);
            g2.drawString(text, x, y);
        }
        if (showCaret) {
            int stringWidth = g2.getFontMetrics(font).stringWidth(text) + 2;
            g2.setColor(Color.GREEN);
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(x + stringWidth, y + 2, x + stringWidth, y -  charHeight);
        }
    }

    public void toggleCaret() {
        showCaret = !showCaret;
    }

    public void append(char c) {
        text += c;
    }

    public void backspace() {
        if (text.length() > 0) {
            text = text.substring(0, text.length() - 1);
        }
    }

    public boolean isEmpty() {
        return text.isEmpty();
    }

    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public int getStrokeWidth() {
        return font.getSize();
    }

    @Override
    public void updateBounds(int x, int y) {
    }
}
