
import java.awt.*;
import java.awt.geom.*;
import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.Force;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;

public class AngryBirds extends Application {

    private ResizableCanvas canvas;
    private World world;
    private MousePicker mousePicker;
    private Camera camera;
    private boolean debugSelected = false;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Point2D.Double beginPosition;
    private Point2D.Double endPosition;
    private boolean ballShot = false;


    @Override
    public void start(Stage stage) throws Exception {

        BorderPane mainPane = new BorderPane();

        // Add debug button
        javafx.scene.control.CheckBox showDebug = new CheckBox("Show debug");
        showDebug.setOnAction(e -> {
            debugSelected = showDebug.isSelected();
        });
        javafx.scene.control.Button reset = new Button("Reset");
        reset.setOnAction(event -> {
            world.removeAllBodies();
            gameObjects.clear();
            try {
                init();
                this.ballShot = false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        HBox hbox = new HBox(showDebug, reset);
        hbox.setSpacing(50);
        mainPane.setTop(hbox);

        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        camera = new Camera(canvas, g -> draw(g), g2d);
        canvas.setOnMousePressed(event -> {
            this.beginPosition = new Point2D.Double(event.getX(), event.getY());
        });
        canvas.setOnMouseReleased(event -> {
            this.endPosition = new Point2D.Double(event.getX(), event.getY());
            fire();
        });

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane, 1920, 1080));
        stage.setTitle("Angry Birds");
        stage.show();
        draw(g2d);
    }

    public void init() throws IOException {
        world = new World();
        world.setGravity(new Vector2(0, -9.8));

        // Vloer
        Body floor = new Body();
        floor.addFixture(Geometry.createRectangle(20, 1));
        floor.getTransform().setTranslation(0, -4.6);
        floor.setMass(MassType.INFINITE);
        world.addBody(floor);
        gameObjects.add(new GameObject("background.jpg", floor, new Vector2(0,-435), 1.05));

        // Wall 1
        Body wall1 = new Body();
        wall1.addFixture(Geometry.createRectangle(1, 50));
        wall1.getTransform().setTranslation(-10.5, 0);
        wall1.setMass(MassType.INFINITE);
        world.addBody(wall1);

        // Wall 2
        Body wall2 = new Body();
        wall2.addFixture(Geometry.createRectangle(1, 50));
        wall2.getTransform().setTranslation(10.5, 0);
        wall2.setMass(MassType.INFINITE);
        world.addBody(wall2);

        // Catapult
        Body catapult = new Body();
        catapult.addFixture(Geometry.createRectangle(0.1, 0.1));
        catapult.getTransform().setTranslation(-6.5, -3.5);
        catapult.setMass(MassType.INFINITE);
        world.addBody(catapult);
        gameObjects.add(new GameObject("Catapult.png", catapult, new Vector2(0,0), 0.5));


        // Bal
        Body ball = new Body();
        ball.addFixture(Geometry.createCircle(0.4));
        ball.getTransform().setTranslation(-6.5, -2.5);
        ball.setMass(MassType.INFINITE);
        ball.getFixture(0).setRestitution(0.75);
        world.addBody(ball);
        gameObjects.add(new GameObject("Red.png", ball, new Vector2(0,0), 0.25));

        // Box 0
        Body box = new Body();
        box.addFixture(Geometry.createRectangle(0.8, 0.8));
        box.getTransform().setTranslation(4, -3);
        box.setMass(MassType.NORMAL);
        world.addBody(box);
        gameObjects.add(new GameObject("woodenBox.png", box, new Vector2(0,0), 0.1));

        // Box 1
        Body box1 = new Body();
        box1.addFixture(Geometry.createRectangle(0.8, 0.8));
        box1.getTransform().setTranslation(5, -3);
        box1.setMass(MassType.NORMAL);
        world.addBody(box1);
        gameObjects.add(new GameObject("woodenBox.png", box1, new Vector2(0,0), 0.1));

        // Box 2
        Body box2 = new Body();
        box2.addFixture(Geometry.createRectangle(0.8, 0.8));
        box2.getTransform().setTranslation(6, -3);
        box2.setMass(MassType.NORMAL);
        world.addBody(box2);
        gameObjects.add(new GameObject("woodenBox.png", box2, new Vector2(0,0), 0.1));

        // Box 3
        Body box3 = new Body();
        box3.addFixture(Geometry.createRectangle(0.8, 0.8));
        box3.getTransform().setTranslation(7, -3);
        box3.setMass(MassType.NORMAL);
        world.addBody(box3);
        gameObjects.add(new GameObject("woodenBox.png", box3, new Vector2(0,0), 0.1));

        // Box 4
        Body box4 = new Body();
        box4.addFixture(Geometry.createRectangle(0.8, 0.8));
        box4.getTransform().setTranslation(8, -3);
        box4.setMass(MassType.NORMAL);
        world.addBody(box4);
        gameObjects.add(new GameObject("woodenBox.png", box4, new Vector2(0,0), 0.1));

        // Box 5
        Body box5 = new Body();
        box5.addFixture(Geometry.createRectangle(0.8, 0.8));
        box5.getTransform().setTranslation(4.5, -2);
        box5.setMass(MassType.NORMAL);
        world.addBody(box5);
        gameObjects.add(new GameObject("woodenBox.png", box5, new Vector2(0,0), 0.1));

        // Box 6
        Body box6 = new Body();
        box6.addFixture(Geometry.createRectangle(0.8, 0.8));
        box6.getTransform().setTranslation(5.5, -2);
        box6.setMass(MassType.NORMAL);
        world.addBody(box6);
        gameObjects.add(new GameObject("woodenBox.png", box6, new Vector2(0,0), 0.1));

        // Box 7
        Body box7 = new Body();
        box7.addFixture(Geometry.createRectangle(0.8, 0.8));
        box7.getTransform().setTranslation(6.5, -2);
        box7.setMass(MassType.NORMAL);
        world.addBody(box7);
        gameObjects.add(new GameObject("woodenBox.png", box7, new Vector2(0,0), 0.1));

        // Box 8
        Body box8 = new Body();
        box8.addFixture(Geometry.createRectangle(0.8, 0.8));
        box8.getTransform().setTranslation(7.5, -2);
        box8.setMass(MassType.NORMAL);
        world.addBody(box8);
        gameObjects.add(new GameObject("woodenBox.png", box8, new Vector2(0,0), 0.1));

        // Box 9
        Body box9 = new Body();
        box9.addFixture(Geometry.createRectangle(0.8, 0.8));
        box9.getTransform().setTranslation(5, -1);
        box9.setMass(MassType.NORMAL);
        world.addBody(box9);
        gameObjects.add(new GameObject("woodenBox.png", box9, new Vector2(0,0), 0.1));

        // Box 10
        Body box10 = new Body();
        box10.addFixture(Geometry.createRectangle(0.8, 0.8));
        box10.getTransform().setTranslation(6, -1);
        box10.setMass(MassType.NORMAL);
        world.addBody(box10);
        gameObjects.add(new GameObject("woodenBox.png", box10, new Vector2(0,0), 0.1));

        // Box 11
        Body box11 = new Body();
        box11.addFixture(Geometry.createRectangle(0.8, 0.8));
        box11.getTransform().setTranslation(7, -1);
        box11.setMass(MassType.NORMAL);
        world.addBody(box11);
        gameObjects.add(new GameObject("woodenBox.png", box11, new Vector2(0,0), 0.1));

        // Box 12
        Body box12 = new Body();
        box12.addFixture(Geometry.createRectangle(0.8, 0.8));
        box12.getTransform().setTranslation(5.5, 0);
        box12.setMass(MassType.NORMAL);
        world.addBody(box12);
        gameObjects.add(new GameObject("woodenBox.png", box12, new Vector2(0,0), 0.1));

        // Box 13
        Body box13 = new Body();
        box13.addFixture(Geometry.createRectangle(0.8, 0.8));
        box13.getTransform().setTranslation(6.5, 0);
        box13.setMass(MassType.NORMAL);
        world.addBody(box13);
        gameObjects.add(new GameObject("woodenBox.png", box13, new Vector2(0,0), 0.1));

        // Box 14
        Body box14 = new Body();
        box14.addFixture(Geometry.createRectangle(0.8, 0.8));
        box14.getTransform().setTranslation(6, 1);
        box14.setMass(MassType.NORMAL);
        world.addBody(box14);
        gameObjects.add(new GameObject("woodenBox.png", box14, new Vector2(0,0), 0.1));
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        AffineTransform originalTransform = graphics.getTransform();


        graphics.setTransform(camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()));
        graphics.scale(1, -1);


        for (GameObject go : gameObjects) {
            go.draw(graphics);
        }

        if (debugSelected) {
            graphics.setColor(Color.blue);
            DebugDraw.draw(graphics, world, 100);
        }

        graphics.setTransform(originalTransform);
    }

    public void update(double deltaTime) {
        world.update(deltaTime);
    }

    private void fire() {
        if (!ballShot) {
            world.getBody(4).setMass(MassType.NORMAL);
            world.getBody(4).applyForce(new Force((this.beginPosition.getX() - this.endPosition.getX()) * 5,
                    (this.beginPosition.getY() - this.endPosition.getY()) * 5));
            ballShot = true;
        }
    }

    public static void main(String[] args) {
        launch(AngryBirds.class);
    }

}
