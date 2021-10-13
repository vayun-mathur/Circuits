package com.vayun.circuit.gui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.StackPane;

public class ElementGUI extends StackPane {

    private final DoubleProperty rotateWire = new SimpleDoubleProperty(0);

    public DoubleProperty rotateWireProperty() {
        return rotateWire;
    }
}
