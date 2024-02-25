import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

public class MovingCharacter extends Application {
    private ResizableCanvas canvas;
    private BufferedImage[] tiles;
    private double current = 32;
    private double x = 0;
    private boolean mousePressed = false;

    @Override
    public void start(Stage stage) throws Exception {
        try {
            BufferedImage image = ImageIO.read(getClass().getResource("/images/sprite.png"));
            tiles = new BufferedImage[65];

            for (int i = 0; i < 65; i++)
                tiles[i] = image.getSubimage(64 * (i % 8), 64 * (i / 8), 64, 64);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        canvas.setOnMousePressed(event -> {
            this.current = 64;
            this.mousePressed = true;
            this.current = 40;
        });
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 1.0e8);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Moving Character");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics) {
        AffineTransform affineTransform = new AffineTransform();
        graphics.setTransform(affineTransform);
        graphics.setBackground(Color.black);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        graphics.drawImage(tiles[(int) Math.floor(this.current)], (int) this.x, 0, null);
    }


    public void update(double deltaTime) {
        this.x += deltaTime * 2;
        if (this.x >= canvas.getWidth()) {
            this.x = -64;
        }

        if (this.mousePressed){
            if ((this.current) <= 47) {
                this.current += deltaTime;
            } else {
                this.current = 64;
                this.mousePressed = false;
            }
        } else {
            if ((this.current) <= 39) {
                this.current += deltaTime;
            } else {
                this.current = 32;
            }
        }
    }
    public void mousePressed(){
        this.current = 64;
    }

    public static void main(String[] args) {
        launch(MovingCharacter.class);
    }

}
