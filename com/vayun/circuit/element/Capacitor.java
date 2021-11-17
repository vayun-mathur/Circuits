package com.vayun.circuit.element;

public class Capacitor extends ResistorCapacitorInductor {
    public Capacitor(String name, double capacitance) {
        super(name, 0, capacitance, 0);
    }
}
