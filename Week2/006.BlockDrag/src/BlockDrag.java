import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.FCMPG;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class BlockDrag extends Application {
    ResizableCanvas canvas;
    ArrayList<Renderable> renderables;
    boolean dragged;
    int selected;
    FXGraphics2D graphics2D;
    int xDiff;
    int yDiff;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Block Dragging");
        primaryStage.show();
        this.graphics2D = new FXGraphics2D(canvas.getGraphicsContext2D());

        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));

        draw(graphics2D);
    }


    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        for (Renderable renderable : renderables) {
            renderable.draw(graphics);
        }
    }


    public static void main(String[] args) {
        launch(BlockDrag.class);
    }

    private void mousePressed(MouseEvent e) {
        for (Renderable renderable : renderables) {
            if (e.getX() >= renderable.getPosition().getX() &&
                    e.getX() <= (renderable.getPosition().getX() + 50) &&
                    e.getY() >= renderable.getPosition().getY() &&
                    e.getY() <= (renderable.getPosition().getY() + 50)) {

                System.out.println(renderable.getPosition().getX() + ", " + renderable.getPosition().getY());
                System.out.println(e.getX() + ", " + e.getY());
                System.out.println(renderables.indexOf(renderable));

                this.xDiff = (int) (e.getX() - renderable.getPosition().getX());
                this.yDiff = (int) (e.getY() - renderable.getPosition().getY());
                dragged = true;
                selected = renderables.indexOf(renderable);
            }
        }
    }

    private void mouseReleased(MouseEvent e) {
        dragged = false;
    }

    private void mouseDragged(MouseEvent e) {
        if (dragged) {
            renderables.set(selected, new Renderable(new Point2D.Double(e.getX() - this.xDiff, e.getY() - this.yDiff), Color.GREEN));
            draw(graphics2D);
        }
    }

    public void init() {
        this.dragged = false;
        this.selected = 0;
        this.xDiff = 0;
        this.yDiff = 0;

        renderables = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            renderables.add(new Renderable(new Point2D.Double((60*i) + 10, 10), Color.GREEN));
        }
    }

}