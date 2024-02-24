import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Rainbow extends Application {
    private ResizableCanvas canvas;
    private Color[] colors;

    @Override
    public void start(Stage stage) throws Exception
    {
        colors = new Color[]{Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED, Color.ORANGE};
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Rainbow");
        stage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight()/2);

        AffineTransform transform = new AffineTransform();
        transform.translate(canvas.getWidth()/2-50, canvas.getHeight()-50);
        transform.rotate(Math.toRadians(242.0f));
        transform.translate(0, -100);
        Font font = new Font("SansSerif", Font.PLAIN, 50);
        String tekst = "Regenboog";
        Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED, Color.ORANGE};

        for (int i = 0; i < tekst.length(); i++) {
            graphics.setColor(colors[i]);
            transform.translate(0, 100);
            transform.rotate(Math.toRadians((double) 180 /(tekst.length()-1)));
            transform.translate(0, -100);
            Shape shape = font.createGlyphVector(graphics.getFontRenderContext(), tekst.substring(i, i+1)).getOutline();
            graphics.fill(transform.createTransformedShape(shape));
        }

    }


    public static void main(String[] args)
    {
        launch(Rainbow.class);
    }

}