import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Spirograph extends Application {
    private TextField v1;
    private TextField v2;
    private TextField v3;
    private TextField v4;

    private Canvas canvas = new Canvas(1920, 1080);


    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox mainBox = new VBox();
        HBox topBar = new HBox();
        mainBox.getChildren().add(topBar);
        mainBox.getChildren().add(new Group(canvas));

        topBar.getChildren().add(v1 = new TextField("300"));
        topBar.getChildren().add(v2 = new TextField("1"));
        topBar.getChildren().add(v3 = new TextField("300"));
        topBar.getChildren().add(v4 = new TextField("1"));

        v1.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));
        v2.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));
        v3.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));
        v4.textProperty().addListener(e -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));

        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(mainBox));
        primaryStage.setTitle("Spirograph");
        primaryStage.show();
    }

    public void draw(FXGraphics2D graphics) {
        double a;
        double b;
        double c;
        double d;

        try {
            a = Double.parseDouble(v1.getText());
            b = Double.parseDouble(v2.getText());
            c = Double.parseDouble(v3.getText());
            d = Double.parseDouble(v4.getText());
        } catch (Exception e) {
            return;
        }

        graphics.setColor(Color.WHITE);
        graphics.fillRect(-2000, -1200, 4000, 2400);
        graphics.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        graphics.scale(1, -1);
        graphics.setColor(Color.black);

        double lastX;
        double lastY;
        float scale = 1;

        for (int i = 0; i < 100; i++) {
            double x = a * Math.cos(b * i) + c * Math.cos(d * i);
            double y = a * Math.sin(b * i) + c * Math.sin(d * i);
            lastX = a * Math.cos(b * (i - 1)) + c * Math.cos(d * (i - 1));
            lastY = a * Math.sin(b * (i - 1)) + c * Math.sin(d * (i - 1));
            graphics.drawLine((int) (x * scale), (int) (y * scale), (int) (lastX * scale), (int) (lastY * scale));
        }
        graphics.translate(-canvas.getWidth() / 2, -canvas.getHeight() / 2);
    }

    public static void main(String[] args) {
        launch(Spirograph.class);
    }

}
