import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Rainbow extends Application {
    private Canvas canvas = new Canvas(1920, 1080);
    @Override
    public void start(Stage primaryStage) throws Exception {
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Rainbow");
        primaryStage.show();
    }
    
    
    public void draw(FXGraphics2D graphics) {
        graphics.translate(this.canvas.getWidth() / 2, 1000);
        graphics.scale(1, -1);
        float scale = 10;
        float step = 100000.0f;
        for (int i = 0; i < step; i++) {
            double cos = Math.cos(i / step * Math.PI);
            double sin = Math.sin(i / step * Math.PI);

            double x1 = (70 * cos);
            double y1 = (70 * sin);
            double x2 = (89 * cos);
            double y2 = (89 * sin);
            graphics.drawLine((int)(x1*scale), (int)(y1*scale), (int)(x2*scale), (int)(y2*scale));

            double x3 = (50 * cos);
            double y3 = (50 * sin);
            double x4 = (69 * cos);
            double y4 = (69 * sin);
            graphics.drawLine((int)(x3*scale), (int)(y3*scale), (int)(x4*scale), (int)(y4*scale));

            double x5 = (30 * cos);
            double y5 = (30 * sin);
            double x6 = (49 * cos);
            double y6 = (49 * sin);
            graphics.drawLine((int)(x5*scale), (int)(y5*scale), (int)(x6*scale), (int)(y6*scale));
            graphics.setColor(Color.getHSBColor(i/step, 1, 1));
        }


    }
    
    
    
    public static void main(String[] args) {
        launch(Rainbow.class);
    }

}
