package com.vayun.circuit.element;

import com.vayun.circuit.element.capacitor.Capacitor;
import com.vayun.circuit.element.resistor.Resistor;

public class SeriesResistorCapacitor extends CircuitElement {

    private final Capacitor c;
    private final Resistor r;

    public SeriesResistorCapacitor(Capacitor c, Resistor r) {
        this.c = c;
        this.r = r;
    }

    @Override
    public void analyseVoltage(double voltage) {
        this.setVoltage(voltage);
        r.analyseVoltage(voltage);
        c.analyseCurrent(r.getCurrent());
        r.analyseVoltage(voltage-c.getVoltage());
        c.analyseCurrent(r.getCurrent());
        this.setCurrent(r.getCurrent());
    }

    @Override
    public void analyseCurrent(double current) {
        this.setCurrent(current);
        r.analyseCurrent(current);
        c.analyseCurrent(current);
        this.setVoltage(r.getVoltage() + c.getVoltage());

    }

    @Override
    public void update(double dt) {
        r.update(dt);
        c.update(dt);
    }
}
