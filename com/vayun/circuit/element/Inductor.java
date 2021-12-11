package com.vayun.circuit.element;

public class Inductor extends ResistorCapacitorInductor {
    public Inductor(String name, double inductance) {
        super(name, 0, 0, 0, inductance);
    }
}
