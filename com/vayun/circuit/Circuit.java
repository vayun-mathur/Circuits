package com.vayun.circuit;

import com.vayun.circuit.element.CircuitElement;

public class Circuit {

    private final double voltage;

    private final CircuitElement circuit;

    public Circuit(double voltage, CircuitElement circuit) {
        this.voltage = voltage;
        this.circuit = circuit;
    }

    public void analyse() {
        this.circuit.analyseVoltage(voltage);
    }

    public void update(double dt) {
        this.circuit.update(dt);
    }
}
