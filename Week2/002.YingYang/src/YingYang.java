import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class YingYang extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Ying Yang");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        GeneralPath white = new GeneralPath();
        white.moveTo(200,100);
        white.curveTo(60, 100, 60, 300, 200, 300);
        white.closePath();

        GeneralPath black = new GeneralPath();
        black.moveTo(200,100);
        black.curveTo(340, 100, 340, 300, 200, 300);
        black.closePath();

        graphics.setColor(Color.BLACK);
        graphics.draw(white);
        graphics.draw(black);
        graphics.fill(black);

        Ellipse2D ellipse2D = new Ellipse2D.Double(150, 200, 100, 100);
        Ellipse2D ellipse2D2 = new Ellipse2D.Double(190, 240, 20, 20);
        Area area = new Area(ellipse2D2);
        Area area2 = new Area(ellipse2D);
        area.subtract(area);
        graphics.draw(area2);
        graphics.fill(area2);
        graphics.setColor(Color.white);
        graphics.draw(ellipse2D2);
        graphics.fill(ellipse2D2);

        Ellipse2D ellipse2D3 = new Ellipse2D.Double(150, 100, 100, 100);
        Ellipse2D ellipse2D4 = new Ellipse2D.Double(190, 140, 20, 20);
        Area area3 = new Area(ellipse2D4);
        Area area4 = new Area(ellipse2D3);
        area.subtract(area3);
        graphics.draw(area4);
        graphics.fill(area4);
        graphics.setColor(Color.BLACK);
        graphics.draw(ellipse2D4);
        graphics.fill(ellipse2D4);
    }


    public static void main(String[] args)
    {
        launch(YingYang.class);
    }

}
