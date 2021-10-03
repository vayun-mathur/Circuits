package com.vayun.circuit.element.capacitor;

public class SeriesCapacitor extends Capacitor {

    private final Capacitor c1, c2;

    public SeriesCapacitor(Capacitor c1, Capacitor c2) {
        super(c1.getCapacitance() + c2.getCapacitance(), 0);
        this.c1 = c1;
        this.c2 = c2;
    }

    public void analyseVoltage(double voltage) {
        System.err.println("Cannot evaluate capacitor based on voltage");
        System.exit(1);
    }

    public void analyseCurrent(double current) {
        this.setCurrent(current);
        this.setVoltage(getCharge() / getCapacitance());
        c1.analyseCurrent(current);
        c2.analyseVoltage(current);
    }
}
