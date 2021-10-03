package com.vayun.circuit.element.resistor;

import com.vayun.circuit.element.CircuitElement;

public class Resistor extends CircuitElement {

    private final double resistance; // measured in ohms

    public Resistor(double resistance) {
        this.resistance = resistance;
    }

    @Override
    public void analyseVoltage(double voltage) {
        this.setVoltage(voltage);
        this.setCurrent(voltage / resistance);
    }

    @Override
    public void analyseCurrent(double current) {
        this.setCurrent(current);
        this.setVoltage(current * resistance);
    }

    @Override
    public void update(double dt) {

    }

    public double getResistance() {
        return resistance;
    }
}
