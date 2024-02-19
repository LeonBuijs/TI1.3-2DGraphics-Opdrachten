import java.awt.*;
import java.awt.geom.*;

import org.jfree.fx.FXGraphics2D;

class Renderable {
    private Color color;
    private Shape shape;
    private Point2D position;


    public Renderable( Point2D position, Color color) {
        this.color = color;
        this.position = position;
        this.shape = new Rectangle2D.Double(this.position.getX(), this.position.getY(), 50, 50);
    }

    public void draw(FXGraphics2D g2d) {
        g2d.setColor(Color.black);
        g2d.draw(this.shape);
        g2d.setColor(this.color);
        g2d.fill(this.shape);
    }

    public Point2D getPosition() {
        return position;
    }
}
