package com.vayun.circuit.gui;

import com.vayun.circuit.element.*;
import javafx.beans.binding.NumberExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ElementGUI extends StackPane {

    private final DoubleProperty rotateWire = new SimpleDoubleProperty(0);
    private final DoubleProperty centerX = new SimpleDoubleProperty(0);
    private final DoubleProperty centerY = new SimpleDoubleProperty(0);

    public ElementGUI(String name, double x, double y, CircuitElement e, Controller controller) {

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
        } else if (e instanceof Node) {
            strokeColor = "#000000";
            fillColor = "#000000";
            radius = 2;
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

        setOnKeyPressed(ke -> {
            if(controller.currSelected.getName().equals(name)) {
                if (ke.getCode() == KeyCode.O) {
                    rotateWireProperty().set(rotateWireProperty().get() + 0.05);
                } else if (ke.getCode() == KeyCode.P) {
                    rotateWireProperty().set(rotateWireProperty().get() - 0.05);
                }
            }
        });

        setOnMousePressed((t) -> {
            controller.orgSceneX = t.getSceneX();
            controller.orgSceneY = t.getSceneY();
            toFront();
        });
        setOnMouseDragged((t) -> {
            double offsetX = t.getSceneX() - controller.orgSceneX;
            double offsetY = t.getSceneY() - controller.orgSceneY;

            setTranslateX(getTranslateX() + offsetX);
            setTranslateY(getTranslateY() + offsetY);

            controller.orgSceneX = t.getSceneX();
            controller.orgSceneY = t.getSceneY();
        });
        if(!(e instanceof Node)) {
            setOnMouseClicked((t) -> controller.currSelected = e);
        }

        centerX.bind(translateXProperty().add(widthProperty().divide(2)));
        centerY.bind(translateYProperty().add(heightProperty().divide(2)));
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

    public DoubleProperty rotateWireProperty() {
        return rotateWire;
    }

    public DoubleProperty centerXProperty() {
        return centerX;
    }

    public DoubleProperty centerYProperty() {
        return centerY;
    }
}
