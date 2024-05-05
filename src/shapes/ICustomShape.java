package shapes;

import java.awt.*;
import java.io.Serializable;

public interface ICustomShape extends Serializable {

    Color getColor();

    int getStrokeWidth();

    void updateBounds(int x, int y);
}
