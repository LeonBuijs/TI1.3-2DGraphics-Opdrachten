import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Spiral extends Application {
    private Canvas canvas = new Canvas(1920, 1080);
    @Override
    public void start(Stage primaryStage) throws Exception {
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Spiral");
        primaryStage.show();
    }
    
    
    public void draw(FXGraphics2D graphics) {
        graphics.translate(this.canvas.getWidth() / 2, this.canvas.getHeight() / 2);
        graphics.scale(1, -1);

        graphics.setColor(Color.red);
        graphics.drawLine(0, 0, 1000, 0);
        graphics.setColor(Color.green);
        graphics.drawLine(0, 0, 0, 1000);
        graphics.setColor(Color.black);

        double resolution = 0.01;
        double lastX = 1 * 0 * Math.cos(0);
        double lastY = 1 * 0 * Math.sin(0);;
        float scale = 10;

        for (double i = 0; i < 100; i += resolution) {
            float x = (float) (1 * i * Math.cos(i));
            float y = (float) (1 * i * Math.sin(i));
            graphics.drawLine((int)(x*scale), (int)(y*scale), (int)((lastX)*scale), (int)(lastY*scale));
            lastX = x;
            lastY = y;
        }
    }
    
    
    
    public static void main(String[] args) {
        launch(Spiral.class);
    }

}
