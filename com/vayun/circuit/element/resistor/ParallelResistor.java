package com.vayun.circuit.element.resistor;

public class ParallelResistor extends Resistor {

    private final Resistor r1, r2;

    public ParallelResistor(Resistor r1, Resistor r2) {
        super(inverse(r1.getResistance(), r2.getResistance()));
        this.r1 = r1;
        this.r2 = r2;
    }

    public void analyseVoltage(double voltage) {
        this.setVoltage(voltage);
        this.setCurrent(voltage / getResistance());
        r1.analyseVoltage(getVoltage());
        r2.analyseVoltage(getVoltage());
    }

    public void analyseCurrent(double current) {
        this.setCurrent(current);
        this.setVoltage(current * getResistance());
        r1.analyseVoltage(getVoltage());
        r2.analyseVoltage(getVoltage());
    }
}
