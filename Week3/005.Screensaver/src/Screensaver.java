import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Screensaver extends Application {
    private ResizableCanvas canvas;

    private Point2D.Double punt1;
    private Point2D.Double punt2;
    private Point2D.Double punt3;
    private Point2D.Double punt4;

    private int punt1XMultiplier = -1;
    private int punt2XMultiplier = -1;
    private int punt3XMultiplier = 1;
    private int punt4XMultiplier = 1;

    private int punt1YMultiplier = -1;
    private int punt2YMultiplier = 1;
    private int punt3YMultiplier = -1;
    private int punt4YMultiplier =-1;

    private ArrayList<Line2D.Double> lines;

    @Override
    public void start(Stage stage) throws Exception
    {

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        canvas.setWidth(1200);
        canvas.setHeight(800);

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now)
            {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Screensaver");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.black);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        graphics.setColor(Color.red);
        lines.add(new Line2D.Double(punt1, punt2));
        lines.add(new Line2D.Double(punt2, punt3));
        lines.add(new Line2D.Double(punt3, punt4));
        lines.add(new Line2D.Double(punt4, punt1));

        for (int i = 0; i < lines.size()/4; i++) {
            graphics.draw(lines.get(lines.size()-1 - i));
            graphics.draw(lines.get(lines.size()-2 - i));
            graphics.draw(lines.get(lines.size()-3 - i));
            graphics.draw(lines.get(lines.size()-4 - i));
        }


    }

    public void init()
    {
        lines = new ArrayList<>();
        punt1 = new Point2D.Double(100, 100);
        punt2 = new Point2D.Double(250, 100);
        punt3 = new Point2D.Double(250, 250);
        punt4 = new Point2D.Double(100, 250);
    }

    public void update(double deltaTime)
    {
        deltaTime *= 200;

        // Punt 1
        if (punt1.getX() <= 0 || punt1.getX() >= canvas.getWidth()){
            punt1XMultiplier *= -1;
        }
        if (punt1.getY() <= 0 || punt1.getY() >= canvas.getHeight()){
            punt1YMultiplier *= -1;
        }

        // Punt 2
        if (punt2.getX() <= 0 || punt2.getX() >= canvas.getWidth()){
            punt2XMultiplier *= -1;
        }
        if (punt2.getY() <= 0 || punt2.getY() >= canvas.getHeight()){
            punt2YMultiplier *= -1;
        }

        // Punt 3
        if (punt3.getX() <= 0 || punt3.getX() >= canvas.getWidth()){
            punt3XMultiplier *= -1;
        }
        if (punt3.getY() <= 0 || punt3.getY() >= canvas.getHeight()){
            punt3YMultiplier *= -1;
        }

        // Punt 4
        if (punt4.getX() <= 0 || punt4.getX() >= canvas.getWidth()){
            punt4XMultiplier *= -1;
        }
        if (punt4.getY() <= 0 || punt4.getY() >= canvas.getHeight()){
            punt4YMultiplier *= -1;
        }



        punt1 = new Point2D.Double(punt1.getX()+(deltaTime * punt1XMultiplier), punt1.getY()+(deltaTime * punt1YMultiplier));
        punt2 = new Point2D.Double(punt2.getX()+(deltaTime * punt2XMultiplier), punt2.getY()+(deltaTime * punt2YMultiplier));
        punt3 = new Point2D.Double(punt3.getX()+(deltaTime * punt3XMultiplier), punt3.getY()+(deltaTime * punt3YMultiplier));
        punt4 = new Point2D.Double(punt4.getX()+(deltaTime * punt4XMultiplier), punt4.getY()+(deltaTime * punt4YMultiplier));
    }

    public static void main(String[] args)
    {
        launch(Screensaver.class);
    }

}
