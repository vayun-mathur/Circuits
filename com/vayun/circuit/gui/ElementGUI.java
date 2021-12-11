package com.vayun.circuit.gui;

import com.vayun.circuit.element.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ElementGUI extends StackPane {

    private final DoubleProperty centerX = new SimpleDoubleProperty(0);
    private final DoubleProperty centerY = new SimpleDoubleProperty(0);

    private final String name;

    private CircuitElement e;

    public static ElementGUI arrowStart = null;

    public ElementGUI(String name, double x, double y, CircuitElement e, Controller controller) {
        this.name = name;
        this.e = e;
        String strokeColor = null, fillColor = null;
        double radius = 0;
        if (e instanceof Resistor) {
            strokeColor = "#ff00ff";
            fillColor = "#ff88ff";
            radius = 20;
        } else if (e instanceof Capacitor) {
            strokeColor = "#00ffff";
            fillColor = "#88ffff";
            radius = 20;
        } else if (e instanceof Inductor) {
            strokeColor = "#ffff00";
            fillColor = "#ffff88";
            radius = 20;
        } else if (e instanceof Node) {
            strokeColor = "#000000";
            fillColor = "#000000";
            radius = 2;
        } else if (e instanceof Diode) {
            strokeColor = "#aa88aa";
            fillColor = "#aaaaaa";
            radius = 20;
        } else if (e instanceof PowerSupply) {
            // Battery
            strokeColor = "#888888";
            fillColor = "#888888";
            radius = 30;
        }

        getChildren().add(createCircle(strokeColor, fillColor, radius));
        if(!(e instanceof Node)) {
            getChildren().add(createText(name));
        }
        setCursor(Cursor.HAND);
        setTranslateX(x);
        setTranslateY(y);

        final ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Delete Element");
        item1.setOnAction(e1 -> controller.removeElement(name));
        MenuItem item2 = new MenuItem("Add Arrow From Here");
        item2.setOnAction(e12 -> arrowStart = ElementGUI.this);
        MenuItem item3 = new MenuItem("Add Arrow To Here");
        item3.setOnAction(e13 -> {
            if(arrowStart != null) {
                controller.addArrow(new Arrow(arrowStart, ElementGUI.this, controller));
                arrowStart = null;
            }
        });
        contextMenu.getItems().addAll(item1, item2, item3);

        setOnMousePressed((t) -> {
            controller.orgSceneX = t.getSceneX();
            controller.orgSceneY = t.getSceneY();
            toFront();

            if (t.isSecondaryButtonDown()) {
                contextMenu.show(this, t.getScreenX(), t.getScreenY());
            }
        });
        setOnMouseDragged((t) -> {
            double offsetX = t.getSceneX() - controller.orgSceneX;
            double offsetY = t.getSceneY() - controller.orgSceneY;

            setRealTranslateX(getRealTranslateX() + offsetX);
            setRealTranslateY(getRealTranslateY() + offsetY);

            controller.orgSceneX = t.getSceneX();
            controller.orgSceneY = t.getSceneY();
        });
        if(!(e instanceof Node)) {
            setOnMouseClicked((t) -> controller.currSelected = this.e);
        }

        centerX.bind(translateXProperty().add(widthProperty().divide(2)));
        centerY.bind(translateYProperty().add(heightProperty().divide(2)));

        realTranslateX = getTranslateX();
        realTranslateY = getTranslateY();
    }

    private static Text createText(String name) {
        Text t = new Text(name);
        t.setFont(new Font(14));
        return t;
    }

    private static Circle createCircle(String strokeColor, String fillColor, double radius) {
        Circle circle = new Circle(0, 0, radius, Color.valueOf(fillColor));
        circle.setStroke(Color.valueOf(strokeColor));
        circle.setStrokeWidth(5);

        return circle;
    }

    private double realTranslateX;
    public void setRealTranslateX(double value) {
        realTranslateX = value;
        setTranslateX(Math.round(realTranslateX/10)*10.0);
    }
    public double getRealTranslateX() {
        return realTranslateX;
    }

    private double realTranslateY;
    public void setRealTranslateY(double value) {
        realTranslateY = value;
        setTranslateY(Math.round(realTranslateY/10)*10.0);
    }
    public double getRealTranslateY() {
        return realTranslateY;
    }

    private final List<Arrow> connections = new ArrayList<>();

    public List<Arrow> getConnections() {
        return connections;
    }

    public String getName() {
        return name;
    }

    public DoubleProperty centerXProperty() {
        return centerX;
    }

    public DoubleProperty centerYProperty() {
        return centerY;
    }

    public CircuitElement getCircuitElement() {
        return e;
    }

    public void setCircuitElement(CircuitElement e) {
        this.e = e;
    }
}
