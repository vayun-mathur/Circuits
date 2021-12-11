package com.vayun.circuit.element;

public class Diode extends ResistorCapacitorInductor {
    public Diode(String name) {
        super(name, 0, Double.POSITIVE_INFINITY, 0, 0);
    }
}
