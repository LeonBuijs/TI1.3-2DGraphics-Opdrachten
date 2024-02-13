import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class GradientPaintExercise extends Application {
    private ResizableCanvas canvas;
    public Point2D position = new Point2D.Double(0,0);

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        position = new Point((int) (canvas.getWidth()/2), (int) (canvas.getHeight()/2));
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("GradientPaint");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        canvas.setOnMouseDragged(event -> {
            position = new Point2D.Double(event.getX(), event.getY());
            draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        });
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        Rectangle2D rectangle2D = new Rectangle(0,0, (int) canvas.getWidth(), (int) canvas.getHeight());


        Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.BLACK};
        float[] fractions = {0.2f, 0.4f, 0.6f, 0.8f, 0.9f, 1.0f};

        RadialGradientPaint gradientPaint = new RadialGradientPaint(position, 200, fractions, colors);
        graphics.setPaint(gradientPaint);
        graphics.fill(rectangle2D);
    }


    public static void main(String[] args)
    {
        launch(GradientPaintExercise.class);
    }

}
