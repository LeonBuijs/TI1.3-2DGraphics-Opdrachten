
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class VerletEngine extends Application {

    // Opdracht 5 snap ik niet
    // Opdracht 6 opslaan niet gelukt

    private ResizableCanvas canvas;
    private ArrayList<Particle> particles = new ArrayList<>();
    private ArrayList<Constraint> constraints = new ArrayList<>();
    private PositionConstraint mouseConstraint = new PositionConstraint(null);

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainPane = new BorderPane();

        Button buttonSave = new Button("Save");
        buttonSave.setOnAction(event -> {
            try {
                FileOutputStream fos = new FileOutputStream("C:\\Users\\leonb\\IdeaProjects\\TI1.3-2DGraphics-Opdrachten\\Week4\\001.Verlet\\src\\SaveFile.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(stage.getScene());
                oos.close();
            } catch (FileNotFoundException e) {
                System.out.println("File niet gevonden");
            } catch (IOException e){
                System.out.println("Opslaan niet gelukt");
            }
        });
        Button buttonLoad = new Button("Load");
        buttonLoad.setOnAction(event -> {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("C:\\Users\\leonb\\IdeaProjects\\TI1.3-2DGraphics-Opdrachten\\Week4\\001.Verlet\\src\\SaveFile.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                stage.setScene((Scene) ois.readObject());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        });

        HBox buttonBar = new HBox(buttonSave, buttonLoad);
        mainPane.setTop(buttonBar);

        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
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

        // Mouse Events
        canvas.setOnMouseClicked(e -> mouseClicked(e));
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Verlet Engine");
        stage.show();
        draw(g2d);
    }

    public void init() {
        for (int i = 0; i < 11; i++) {
            particles.add(new Particle(new Point2D.Double(100 + 50 * i, 100)));
        }

        for (int i = 0; i < 10; i++) {
            constraints.add(new DistanceConstraint(particles.get(i), particles.get(i + 1)));
        }

        constraints.add(new PositionConstraint(particles.get(10)));
        constraints.add(mouseConstraint);
    }

    private void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        for (Constraint c : constraints) {
            c.draw(graphics);
        }

        for (Particle p : particles) {
            p.draw(graphics);
        }
    }

    private void update(double deltaTime) {
        for (Particle p : particles) {
            p.update((int) canvas.getWidth(), (int) canvas.getHeight());
        }

        for (Constraint c : constraints) {
            c.satisfy();
        }
    }

    private void mouseClicked(MouseEvent e) {
        Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
        Particle nearest = getNearest(mousePosition);
        Particle newParticle = new Particle(mousePosition);


        if (!(e.getButton() == MouseButton.SECONDARY && e.isShiftDown())) {
            if (mousePosition.distance(nearest.getPosition()) > 10) {
                particles.add(newParticle);
            }
        }
        if (e.isControlDown() && e.getButton() == MouseButton.SECONDARY) {
            constraints.add(new DistanceConstraint(newParticle, nearest, 100));
        } else if (e.isControlDown() && e.getButton() == MouseButton.PRIMARY) {
            constraints.add(new PositionConstraint(newParticle));
        } else if (e.getButton() == MouseButton.SECONDARY && e.isShiftDown()) {
            // geen constraint toevoegen met de nieuwe particle
        } else {
            constraints.add(new DistanceConstraint(newParticle, nearest));
        }


        if (e.getButton() == MouseButton.SECONDARY) {
            ArrayList<Particle> sorted = new ArrayList<>();
            sorted.addAll(particles);

            //sorteer alle elementen op afstand tot de muiscursor. De toegevoegde particle staat op 0, de nearest op 1, en de derde op 2
            Collections.sort(sorted, new Comparator<Particle>() {
                @Override
                public int compare(Particle o1, Particle o2) {
                    return (int) (o1.getPosition().distance(mousePosition) - o2.getPosition().distance(mousePosition));
                }
            });

            if (e.isControlDown()) {
                constraints.add(new DistanceConstraint(newParticle, sorted.get(2), 100));
            } else if (e.isShiftDown()) {
                constraints.add(new DistanceConstraint(sorted.get(0), sorted.get(1)));
            } else {
                constraints.add(new DistanceConstraint(newParticle, sorted.get(2)));
            }

        } else if (e.getButton() == MouseButton.MIDDLE) {
            // Reset
            particles.clear();
            constraints.clear();
            init();
        }
    }

    private Particle getNearest(Point2D point) {
        Particle nearest = particles.get(0);
        for (Particle p : particles) {
            if (p.getPosition().distance(point) < nearest.getPosition().distance(point)) {
                nearest = p;
            }
        }
        return nearest;
    }

    private void mousePressed(MouseEvent e) {
        Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
        Particle nearest = getNearest(mousePosition);
        if (nearest.getPosition().distance(mousePosition) < 10) {
            mouseConstraint.setParticle(nearest);
        }
    }

    private void mouseReleased(MouseEvent e) {
        mouseConstraint.setParticle(null);
    }

    private void mouseDragged(MouseEvent e) {
        mouseConstraint.setFixedPosition(new Point2D.Double(e.getX(), e.getY()));
    }

    public static void main(String[] args) {
        launch(VerletEngine.class);
    }

}
