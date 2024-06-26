import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;


public class Eindopdracht extends Application {
    private ResizableCanvas canvas;
    private boolean ballMoved = false;
    private int lightX = 425;
    private int lightY = 425;
    private ArrayList<Point2D> corners = new ArrayList<>();
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private ArrayList<Line2D> lines = new ArrayList<>();
    private Rectangle2D border;
    private Area area = createArea();

    @Override
    public void init() {
        this.obstacles.add(new Obstacle(400, 100, 100, 100));
        this.obstacles.add(new Obstacle(400, 700, 100, 100));
        this.obstacles.add(new Obstacle(700, 400, 100, 100));
    }

    @Override
    public void start(Stage stage) throws Exception {


        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);


        canvas.setOnMousePressed(event -> {
            if (event.getX() > this.lightX && event.getX() < lightX + 50 &&
                    event.getY() > this.lightY && event.getY() < lightY + 50) {
                this.ballMoved = true;
            }
            for (Obstacle obstacle : obstacles) {
                if (obstacle.isPressed(new Point2D.Double(event.getX(), event.getY()))) {
                    obstacle.setMoving(true);
                }
            }
        });
        canvas.setOnMouseReleased(event -> {
            if (ballMoved) {
                this.ballMoved = false;
            }
            for (Obstacle obstacle : obstacles) {
                obstacle.setMoving(false);
            }
        });
        canvas.setOnMouseDragged(event -> {
            if (ballMoved) {
                if (event.getX() - 35 > 0 && event.getX() + 35 < canvas.getWidth()) {
                    this.lightX = (int) (event.getX() - 25);
                }
                if (event.getY() - 35 > 0 && event.getY() + 35 < canvas.getHeight()) {
                    this.lightY = (int) event.getY() - 25;
                }
            }
            for (Obstacle obstacle : obstacles) {
                if (obstacle.isMoving()) {
                    obstacle.setX((int) (event.getX() - obstacle.getWidth() / 2));
                    obstacle.setY((int) (event.getY() - obstacle.getHeight() / 2));
                    obstacle.updateShape();
                }
            }
        });


        javafx.scene.control.TextField width = new javafx.scene.control.TextField("Width");
        javafx.scene.control.TextField height = new javafx.scene.control.TextField("Height");
        javafx.scene.control.Button create = new javafx.scene.control.Button("Create Block");
        javafx.scene.control.Button reset = new javafx.scene.control.Button("Reset");
        create.setOnAction(event -> {
            try {
                if (Integer.parseInt(width.getText()) >= 15 && Integer.parseInt(height.getText()) >= 15) {
                    obstacles.add(new Obstacle(10, 10, Integer.parseInt(width.getText()), Integer.parseInt(height.getText())));
                } else {
                    System.out.println("Afmetingen zijn te klein");
                }
            } catch (NumberFormatException e){
                System.out.println("Vul een getal in!");
            }
        });
        reset.setOnAction(event -> {
            obstacles.clear();
            this.obstacles.add(new Obstacle(400, 100, 100, 100));
            this.obstacles.add(new Obstacle(400, 700, 100, 100));
            this.obstacles.add(new Obstacle(700, 400, 100, 100));
        });
        mainPane.setTop(new HBox(width, height, create, reset));

        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane, 900, 900));

        stage.setTitle("LightSimulator");
        stage.show();
        draw(g2d);


    }

    private void update(double deltatime) {
        corners.clear();

        corners.add(new Point2D.Double(0, 0));
        corners.add(new Point2D.Double(canvas.getWidth(), 0));
        corners.add(new Point2D.Double(0, canvas.getHeight()));
        corners.add(new Point2D.Double(canvas.getWidth(), canvas.getHeight()));

        for (Obstacle obstacle : obstacles) {
            corners.add(new Point2D.Double(obstacle.getX(), obstacle.getY()));
            corners.add(new Point2D.Double(obstacle.getX() + obstacle.getWidth(), obstacle.getY()));
            corners.add(new Point2D.Double(obstacle.getX(), obstacle.getY() + obstacle.getHeight()));
            corners.add(new Point2D.Double(obstacle.getX() + obstacle.getWidth(), obstacle.getY() + obstacle.getHeight()));
        }
        lines.clear();

        Line2D.Double line = null;
        for (Point2D corner : corners) {
            line = new Line2D.Double(corner.getX(), corner.getY(), this.lightX + 25, this.lightY + 25);
            line = extendLine(line);

            boolean addLine = true;
            for (Obstacle obstacle : obstacles) {
                if (isLineIntersectingSquare(line, obstacle)) {
                    addLine = false;
                }
            }
            if (addLine){
                lines.add(line);
            }
        }

        this.area = createArea();
    }

    private void draw(FXGraphics2D g) {
        // Clear screen
        g.setTransform(new AffineTransform());
        g.setBackground(Color.black);
        g.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        float[] fractions = {0.1f, 0.7f};
        Color[] colors = {Color.getHSBColor((float) 43 / 255, 1, (float) 0.85), Color.black};

        RadialGradientPaint paint = new RadialGradientPaint(new Point2D.Double(this.lightX + 25, this.lightY + 25), (int) canvas.getWidth(), fractions, colors);
        g.setPaint(paint);
        g.fill(this.area);

        g.setColor(Color.yellow);
        g.fill(new Ellipse2D.Double(this.lightX, this.lightY, 50, 50));

        g.setColor(Color.gray);
        for (Obstacle obstacle : obstacles) {
            g.fill(obstacle.getRectangle());
        }
    }

    private boolean isLineIntersectingSquare(Line2D.Double line, Obstacle test) {
        Rectangle2D.Double rect = new Rectangle2D.Double(test.getX() + 1, test.getY() + 1, test.getWidth() - 2, test.getHeight() - 2);
        if (line.intersects(rect)) {
            return true;
        }
        return false;
    }

    private Line2D.Double extendLine(Line2D.Double line) {
        Line2D.Double oldLine = line;
        double rc = (line.getY1() - line.getY2()) / (line.getX1() - line.getX2());
        double xDiff;
        if (line.getX1() - line.getX2() < 0) {
            xDiff = -0.1;
        } else if (line.getX1() - line.getX2() > 0) {
            xDiff = 0.1;
        } else {
            xDiff = 0;
        }


        while (line.getX2() > 0 && line.getX2() < canvas.getWidth() && line.getY2() > 0 && line.getY2() < canvas.getHeight()) {
            // Lijn verlengen
            line = new Line2D.Double(this.lightX + 25, this.lightY + 25, line.getX2() + xDiff, line.getY2() + (xDiff * rc));
        }

        for (Obstacle obstacle : obstacles) {
            if (isLineIntersectingSquare(line, obstacle)) {
                return oldLine;
            }
        }

        return line;
    }

    private Area createArea() {

        Collections.sort(lines, (lineA, lineB) -> {
            double angleA = Math.atan2(lineA.getY2() - lineA.getY1(), lineA.getX2() - lineA.getX1());
            double angleB = Math.atan2(lineB.getY2() - lineB.getY1(), lineB.getX2() - lineB.getX1());
            return Double.compare(angleA, angleB);
        });

        Area complete = new Area();

        for (int i = 0; i < lines.size(); i++) {
            Line2D currentLine = lines.get(i);
            Line2D nextLine;

            if (i + 1 >= lines.size()) {
                nextLine = lines.get(0);
            } else {
                nextLine = lines.get(i + 1);
            }

            GeneralPath path = new GeneralPath();
            path.moveTo(this.lightX + 25, this.lightY + 25);
            path.lineTo(currentLine.getX2(), currentLine.getY2());
            path.lineTo(nextLine.getX2(), nextLine.getY2());
            path.lineTo(this.lightX + 25, this.lightY + 25);
            path.closePath();

            Area area = new Area(path);
            for (Obstacle obstacle : obstacles) {
                Rectangle2D.Double obstacleSmall = new Rectangle2D.Double(obstacle.getX() + 5, obstacle.getY() + 5, obstacle.getWidth() - 10, obstacle.getHeight() - 10);
                boolean intersects = true;
                try {
                    intersects = area.intersects(obstacleSmall);
                } catch (Exception e) {
                    // Ik weet niet hoe je dit anders kunt oplossen
                }

                if (intersects) {
                    // Maak de area kleiner
                    path = new GeneralPath();
                    path.moveTo(this.lightX + 25, this.lightY + 25);

                    Point2D.Double point1 = obstacle.getClosestCorner(currentLine);
                    path.lineTo(point1.getX(), point1.getY());

                    Point2D.Double point2 = obstacle.getClosestCorner(nextLine);
                    path.lineTo(point2.getX(), point2.getY());
                    path.closePath();

                    area = new Area(path);
                }
            }

            try {
                complete.add(area);

            } catch (ClassCastException e) {
                // Ik weet niet hoe je dit anders kunt oplossen
            }
        }
        return complete;
    }
}
