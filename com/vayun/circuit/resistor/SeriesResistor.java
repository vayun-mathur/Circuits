package com.vayun.circuit.resistor;

public class SeriesResistor extends Resistor {

    private final Resistor r1, r2;

    public SeriesResistor(Resistor r1, Resistor r2) {
        super(r1.getResistance()+r2.getResistance());
        this.r1 = r1;
        this.r2 = r2;
    }

    public void analyseVoltage(double voltage) {
        this.setVoltage(voltage);
        this.setCurrent(voltage / getResistance());
        r1.analyseCurrent(getCurrent());
        r2.analyseCurrent(getCurrent());
    }

    public void analyseCurrent(double current) {
        this.setCurrent(current);
        this.setVoltage(current * getResistance());
        r1.analyseCurrent(getCurrent());
        r2.analyseCurrent(getCurrent());
    }
}
