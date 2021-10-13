package com.vayun.circuit.gui;

import com.vayun.circuit.Circuit;
import com.vayun.circuit.element.CircuitElement;
import com.vayun.circuit.element.PowerSupply;
import com.vayun.circuit.element.ResistorCapacitor;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.concurrent.ConcurrentHashMap;

public class Controller {

    public static Circuit circuit;

    public volatile CircuitElement currSelected;

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

    @FXML
    private VBox mainPane;

    private double t = 0;

    DoubleProperty rotation;

    public void initialize() {
        mainPane.requestFocus();
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
                Controller.this.handle();
            }
        };

        mainPane.setOnKeyPressed((t)->{
            for(ElementGUI p: elements_screen.values()) {
                p.getOnKeyPressed().handle(t);
            }
        });

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
        if(currSelected instanceof PowerSupply) {
            setPowerSupply((PowerSupply)currSelected);
        }


        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private final ConcurrentHashMap<String, ElementGUI> elements_screen = new ConcurrentHashMap<>();

    public void drawCircuit() {
        circuitCanvas.getChildren().clear();
        int c = 0;
        for (CircuitElement e : circuit.getElements()) {
            if (e instanceof ResistorCapacitor) {
                if (((ResistorCapacitor) e).getResistance() != 0) {
                    //Resistor
                    elements_screen.put(e.getName(), createComponent(e.getName(), "#ff00ff", "#ff88ff", 50 + c * 30, 100));
                } else {
                    //Capacitor
                    elements_screen.put(e.getName(), createComponent(e.getName(), "#00ffff", "#88ffff", 50 + c * 30, 100));
                }
            }
            if(e instanceof PowerSupply)
                // Battery
                elements_screen.put(e.getName(), createComponent(e.getName(), "#888888", "#888888", 50 + c * 30, 100));

            elements_screen.get(e.getName()).setOnMouseClicked((t) -> currSelected = e);
            c++;
        }
        circuitCanvas.getChildren().addAll(elements_screen.values());
        for (Circuit.Connection conn : circuit.getConnections()) {
            circuitCanvas.getChildren().add(connect(elements_screen.get(conn.component1), elements_screen.get(conn.component2)));
        }
        for (ElementGUI p : elements_screen.values()) {
            p.toFront();
        }
    }

    private static DoubleBinding cos(ObservableNumberValue other) {
        return new DoubleBinding() {
            {
                bind(other);
            }
            @Override
            protected double computeValue() {
                return Math.cos(other.doubleValue());
            }
        };
    }

    private static DoubleBinding sin(ObservableNumberValue other) {
        return new DoubleBinding() {
            {
                bind(other);
            }
            @Override
            protected double computeValue() {
                return Math.sin(other.doubleValue());
            }
        };
    }

    private Arrow connect(ElementGUI c1, ElementGUI c2) {
        Arrow line = new Arrow();

        line.startXProperty().bind(c1.translateXProperty().add(c1.widthProperty().divide(2)).add(
                c1.widthProperty().divide(2).multiply(cos(c1.rotateWireProperty()))
        ));

        line.startYProperty().bind(c1.translateYProperty().add(c1.heightProperty().divide(2)).add(
                c1.heightProperty().divide(2).multiply(sin(c1.rotateWireProperty()))
        ));

        line.endXProperty().bind(c2.translateXProperty().add(c2.widthProperty().divide(2)).subtract(
                c2.widthProperty().divide(2).multiply(cos(c2.rotateWireProperty()))
        ));

        line.endYProperty().bind(c2.translateYProperty().add(c2.heightProperty().divide(2)).subtract(
                c2.heightProperty().divide(2).multiply(sin(c2.rotateWireProperty()))
        ));

        return line;
    }

    double orgSceneX, orgSceneY;

    private ElementGUI createComponent(String name, String strokeColor, String fillColor, double x, double y) {
        ElementGUI stack = new ElementGUI();
        stack.getChildren().addAll(createCircle(strokeColor, fillColor), createText(name));
        stack.setCursor(Cursor.HAND);
        stack.setTranslateX(x);
        stack.setTranslateY(y);

        stack.setOnKeyPressed(ke -> {
            if(currSelected.getName().equals(name)) {
                if (ke.getCode() == KeyCode.O) {
                    stack.rotateWireProperty().set(stack.rotateWireProperty().get() + 0.05);
                } else if (ke.getCode() == KeyCode.P) {
                    stack.rotateWireProperty().set(stack.rotateWireProperty().get() - 0.05);
                }
            }
        });

        stack.setOnMousePressed((t) -> {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
            stack.toFront();
        });
        stack.setOnMouseDragged((t) -> {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;

            stack.setTranslateX(stack.getTranslateX() + offsetX);
            stack.setTranslateY(stack.getTranslateY() + offsetY);

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
        rotation = elements_screen.get(c.getName()).rotateProperty();
    }

    public void setResistor(ResistorCapacitor r) {
        resistorPane.setVisible(true);
        capacitorPane.setVisible(false);
        resistorName.setText("Component: " + r.getName());
        resistorVoltage.setText(String.format("Voltage: %.3fV", r.getVoltage()));
        resistorCurrent.setText(String.format("Current: %.3fA", r.getCurrent()));
        rotation = elements_screen.get(r.getName()).rotateProperty();
    }

    private void setPowerSupply(PowerSupply p) {
        resistorPane.setVisible(true);
        capacitorPane.setVisible(false);
        resistorName.setText("Component: " + p.getName());
        resistorVoltage.setText(String.format("Voltage: %.3fV", p.getVoltage()));
        resistorCurrent.setText(String.format("Current: %.3fA", p.getCurrent()));
        rotation = elements_screen.get(p.getName()).rotateProperty();
    }
}
