import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import java.awt.geom.Line2D;

public class House extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1024, 768);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("House");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {
        // Buitenkant
        graphics.draw(new Line2D.Double(512, 10, 256, 200));
        graphics.draw(new Line2D.Double(512, 10, 768, 200));
        graphics.draw(new Line2D.Double(256, 200, 256, 600));
        graphics.draw(new Line2D.Double(768, 200, 768, 600));
        graphics.draw(new Line2D.Double(256, 600, 768, 600));

        // Deur
        graphics.draw(new Line2D.Double(300, 600, 300, 400));
        graphics.draw(new Line2D.Double(300, 400, 400, 400));
        graphics.draw(new Line2D.Double(400, 400, 400, 600));

        // Raam
        graphics.draw(new Line2D.Double(500, 300, 700, 300));
        graphics.draw(new Line2D.Double(500, 300, 500, 500));
        graphics.draw(new Line2D.Double(500, 500, 700, 500));
        graphics.draw(new Line2D.Double(700, 300, 700, 500));
    }



    public static void main(String[] args) {
        launch(House.class);
    }

}
