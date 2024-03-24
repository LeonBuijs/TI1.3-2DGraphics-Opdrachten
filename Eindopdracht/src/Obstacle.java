import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Obstacle {
    private int x;
    private int y;
    private int width;
    private int height;
    private Rectangle2D rectangle;
    private boolean isMoving = false;

    public Obstacle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rectangle = new Rectangle2D.Double(this.x, this.y, this.width, this.height);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Rectangle2D getRectangle() {
        return rectangle;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean isPressed(Point2D point){
        if (point.getX() > this.x &&
            point.getX() < this.x + this.width &&
            point.getY() > this.y &&
            point.getY() < this.y + this.height){
            this.isMoving = true;
            return true;
        }
        return false;
    }

    public void updateShape(){
        this.rectangle = new Rectangle2D.Double(this.x, this.y, this.width, this.height);
    }


    public Point2D.Double getClosestCorner(Line2D line){
        Point2D.Double closestCorner = null;
        double closestDistance = Integer.MAX_VALUE;

        Point2D.Double checkingPoint = new Point2D.Double(this.x, this.y);
        if (line.ptLineDist(checkingPoint) < closestDistance){
            closestCorner = checkingPoint;
            closestDistance = line.ptLineDist(checkingPoint);
        }

        checkingPoint = new Point2D.Double(this.x + this.width, this.y);
        if (line.ptLineDist(checkingPoint) < closestDistance){
            closestCorner = checkingPoint;
            closestDistance = line.ptLineDist(checkingPoint);
        }

        checkingPoint = new Point2D.Double(this.x, this.y + this.height);
        if (line.ptLineDist(checkingPoint) < closestDistance){
            closestCorner = checkingPoint;
            closestDistance = line.ptLineDist(checkingPoint);
        }

        checkingPoint = new Point2D.Double(this.x + this.width, this.y + this.height);
        if (line.ptLineDist(checkingPoint) < closestDistance){
            closestCorner = checkingPoint;
            closestDistance = line.ptLineDist(checkingPoint);
        }

        if (closestCorner == null){
            closestCorner = new Point2D.Double(line.getX2(), line.getY2());
        }

        return closestCorner;
    }
}
