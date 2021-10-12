package com.vayun.circuit.gui;

import com.vayun.circuit.Circuit;
import com.vayun.circuit.element.CircuitElement;
import com.vayun.circuit.element.ResistorCapacitor;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

public class Window extends Application {

    public static Circuit circuit;

    public CircuitElement currSelected;

    @FXML
    private Pane resistorPane;
    @FXML
    private Label resistorName;
    @FXML
    private Label resistorVoltage;
    @FXML
    private Label resistorCurrent;

    @FXML
    private Pane capacitorPane;
    @FXML
    private Label capacitorName;
    @FXML
    private Label capacitorVoltage;
    @FXML
    private Label capacitorCharge;

    @FXML
    private ListView<String> componentsList;

    @FXML
    private Label leftstatus;

    @FXML
    private Pane circuitCanvas;

    private double t = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("Circuits");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
    }

    public void initialize() {
        for (CircuitElement e : circuit.getElements()) {
            componentsList.getItems().add(e.getName());
        }
        componentsList.getItems().sort(String::compareTo);

        componentsList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> currSelected = circuit.getElement(newValue));

        currSelected = circuit.getElement("R1");
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Window.this.handle();
            }
        };

        drawCircuit();
        timer.start();
    }

    public void handle() {
        circuit.analyse();
        circuit.update(0.02);
        t += 0.02;
        leftstatus.setText(String.format("t = %.2f seconds", t));

        if (currSelected instanceof ResistorCapacitor) {
            if (((ResistorCapacitor) currSelected).getResistance() != 0) {
                setResistor((ResistorCapacitor) currSelected);
            } else {
                setCapacitor((ResistorCapacitor) currSelected);
            }
        }


        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void drawCircuit() {
        circuitCanvas.getChildren().clear();
        Map<String, StackPane> elements_screen = new HashMap<>();
        int c = 0;
        elements_screen.put("B", createComponent("B", "#888888", "#888888", 20, 100));
        for (CircuitElement e : circuit.getElements()) {
            if (e instanceof ResistorCapacitor) {
                if (((ResistorCapacitor) e).getResistance() != 0) {
                    //Resistor
                    elements_screen.put(e.getName(), createComponent(e.getName(),"#ff00ff", "#ff88ff", 50 + c * 30, 100));
                } else {
                    //Capacitor
                    elements_screen.put(e.getName(), createComponent(e.getName(),"#00ffff", "#88ffff", 50 + c * 30, 100));
                }
                elements_screen.get(e.getName()).setOnMouseClicked((t) -> currSelected = e);
            }
            c++;
        }
        circuitCanvas.getChildren().addAll(elements_screen.values());
        for (Circuit.Connection conn : circuit.getConnections()) {
            circuitCanvas.getChildren().add(connect(elements_screen.get(conn.component1), elements_screen.get(conn.component2)));
        }
        for (StackPane p: elements_screen.values()) {
            p.toFront();
        }
    }

    private Arrow connect(StackPane c1, StackPane c2) {
        Arrow line = new Arrow();

        line.startXProperty().bind(c1.translateXProperty().add(c1.widthProperty().divide(2)));
        line.startYProperty().bind(c1.translateYProperty().add(c1.heightProperty().divide(2)));

        line.endXProperty().bind(c2.translateXProperty().add(c2.widthProperty().divide(2)));
        line.endYProperty().bind(c2.translateYProperty().add(c2.heightProperty().divide(2)));

        return line;
    }

    double orgSceneX, orgSceneY;

    private StackPane createComponent(String name, String strokeColor, String fillColor, double x, double y) {
        StackPane stack = new StackPane();
        stack.getChildren().addAll(createCircle(strokeColor, fillColor), createText(name));
        stack.setCursor(Cursor.HAND);
        stack.setTranslateX(x);
        stack.setTranslateY(y);
        stack.setOnMousePressed((t) -> {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();

            StackPane c = (StackPane)(t.getSource());
            c.toFront();
        });
        stack.setOnMouseDragged((t) -> {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;

            StackPane c = (StackPane)(t.getSource());

            c.setTranslateX(c.getTranslateX() + offsetX);
            c.setTranslateY(c.getTranslateY() + offsetY);

            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
        });
        return stack;
    }

    private Text createText(String name) {
        Text t = new Text(name);
        t.setFont(new Font(14));
        return t;
    }

    private Circle createCircle(String strokeColor, String fillColor) {
        Circle circle = new Circle(0, 0, 20, Color.valueOf(fillColor));
        circle.setStroke(Color.valueOf(strokeColor));
        circle.setStrokeWidth(5);

        return circle;
    }

    public void setCapacitor(ResistorCapacitor c) {
        resistorPane.setVisible(false);
        capacitorPane.setVisible(true);
        capacitorName.setText("Component: " + c.getName());
        capacitorVoltage.setText(String.format("Voltage: %.3fV", c.getVoltage()));
        capacitorCharge.setText(String.format("Charge: %.3fC", c.getCharge()));
    }

    public void setResistor(ResistorCapacitor r) {
        resistorPane.setVisible(true);
        capacitorPane.setVisible(false);
        resistorName.setText("Component: " + r.getName());
        resistorVoltage.setText(String.format("Voltage: %.3fV", r.getVoltage()));
        resistorCurrent.setText(String.format("Current: %.3fA", r.getCurrent()));
    }

    public static void run() {
        launch();
    }
}
