package com.vayun.circuit.gui;

import com.vayun.circuit.Circuit;
import com.vayun.circuit.element.*;
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
            if(!(e instanceof Node))
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

        mainPane.setOnKeyPressed((t) -> {
            for (ElementGUI p : elements_screen.values()) {
                p.getOnKeyPressed().handle(t);
            }
        });

        drawCircuit();

        timer.start();
    }

    public void handle() {

        try {
            circuit.analyse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        circuit.update(0.02);
        t += 0.02;
        leftstatus.setText(String.format("t = %.2f seconds", t));

        if (currSelected instanceof Resistor) {
            setResistor((Resistor) currSelected);
        }
        if (currSelected instanceof Capacitor) {
            setCapacitor((Capacitor) currSelected);
        }
        if (currSelected instanceof PowerSupply) {
            setPowerSupply((PowerSupply) currSelected);
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
            elements_screen.put(e.getName(), new ElementGUI(e.getName(), 50 + c * 30, 100, e, this));
            c++;
        }
        circuitCanvas.getChildren().addAll(elements_screen.values());
        for (Circuit.Connection conn : circuit.getConnections()) {
            circuitCanvas.getChildren().add(new Arrow(elements_screen.get(conn.component1), elements_screen.get(conn.component2)));
        }
        elements_screen.values().forEach(ElementGUI::toFront);
    }

    double orgSceneX, orgSceneY;

    public void setCapacitor(Capacitor c) {
        resistorPane.setVisible(false);
        capacitorPane.setVisible(true);
        capacitorName.setText("Component: " + c.getName());
        capacitorVoltage.setText(String.format("Voltage: %.3fV", c.getVoltage()));
        capacitorCharge.setText(String.format("Charge: %.3fC", c.getCharge()));
        rotation = elements_screen.get(c.getName()).rotateProperty();
    }

    public void setResistor(Resistor r) {
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
