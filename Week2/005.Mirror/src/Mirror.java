import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Mirror extends Application {
    ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        canvas.resize(1280, 720);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Mirror");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());




        float resolution = 0.1f;
        float scale = 100;
        for(float x1 = -10; x1 < 10; x1 += resolution)
        {
            float x2 = x1 + resolution;
            float y1 = formule(x1);
            float y2 = formule(x2);
            graphics.drawLine((int)(x1*scale), (int)(y1*scale), (int)(x2*scale), (int)(y2*scale));
        }

        Shape originalSquare = new Rectangle2D.Double(0, 150, 100, 100);
        graphics.setColor(Color.RED);
        graphics.draw(originalSquare);

        AffineTransform transform = new AffineTransform();
        transform.setTransform((2/(1+Math.pow(2.5, 2)))-1,
                ((2*2.5)/(1+Math.pow(2.5,2))),
                ((2*2.5)/(1+Math.pow(2.5,2))),
                ((2*Math.pow(2.5,2))/(1+Math.pow(2.5,2)) -1),
                0, 0);


        Shape mirroredSquare = transform.createTransformedShape(originalSquare);
        graphics.setColor(Color.BLUE);
        graphics.draw(mirroredSquare);
    }
    private float formule(float x) {
        return (float) (2.5*x);
    }


    public static void main(String[] args)
    {
        launch(Mirror.class);
    }

}
