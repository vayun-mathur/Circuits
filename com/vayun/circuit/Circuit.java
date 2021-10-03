package com.vayun.circuit;

import com.vayun.circuit.resistor.Resistor;

public class Circuit {

    private final double voltage;

    private final Resistor resistor;

    public Circuit(double voltage, Resistor resistor) {
        this.voltage = voltage;
        this.resistor = resistor;
    }

    public void analyse() {
        this.resistor.analyseVoltage(voltage);
    }
}
