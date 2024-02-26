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

public class FadingImage extends Application {
    private ResizableCanvas canvas;
    private float imgOpacity = 0.0f;
    private int img1 = 0;
    private int img2 = 1;
    private Image[] images;

    @Override
    public void start(Stage stage) throws Exception {
        images = new Image[]{ImageIO.read(getClass().getResource("image1.jpg")),
                ImageIO.read(getClass().getResource("image2.jpg")),
                ImageIO.read(getClass().getResource("image3.jpg")),
                ImageIO.read(getClass().getResource("image4.jpg")),
                ImageIO.read(getClass().getResource("image5.jpg"))};

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
		if(last == -1)
                    last = now;
		update((now - last) / 0.5e10);
		last = now;
		draw(g2d);
            }
        }.start();
        
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Fading image");
        stage.show();
        draw(g2d);
    }
    
    
    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int)canvas.getWidth(), (int)canvas.getHeight());

        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - this.imgOpacity));
        graphics.drawImage(images[img1], 0,0, null);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.imgOpacity));
        graphics.drawImage(images[img2], 0,0, null);
    }
    

    public void update(double deltaTime) {
        if (this.imgOpacity + deltaTime > 1.0f){
            this.imgOpacity = 0;
            img1++;
            img2++;
            if (img1 >= 5){
                img1 = 0;
            }
            if (img2 >= 5){
                img2 = 0;
            }
        }
	    this.imgOpacity += (float) deltaTime;
    }

    public static void main(String[] args) {
        launch(FadingImage.class);
    }

}
