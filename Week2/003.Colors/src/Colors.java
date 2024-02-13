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

public class Colors extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Colors");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        ArrayList colors = new ArrayList();
        colors.add(Color.BLACK);
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.DARK_GRAY);
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.LIGHT_GRAY);
        colors.add(Color.MAGENTA);
        colors.add(Color.ORANGE);
        colors.add(Color.PINK);
        colors.add(Color.RED);
        colors.add(Color.WHITE);
        colors.add(Color.YELLOW);

        for (int i = 0; i < 13; i++) {
            graphics.setColor((Color) colors.get(i));
            Rectangle2D rectangle2D = new Rectangle(i*100,0, 100, 100);
            graphics.draw(rectangle2D);
            graphics.fill(rectangle2D);
        }
    }


    public static void main(String[] args)
    {
        launch(Colors.class);
    }

}
