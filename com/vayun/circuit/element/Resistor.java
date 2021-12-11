package com.vayun.circuit.element;

public class Resistor extends ResistorCapacitorInductor {
    public Resistor(String name, double resistance) {
        super(name, resistance, resistance, 0, 0);
    }
}
