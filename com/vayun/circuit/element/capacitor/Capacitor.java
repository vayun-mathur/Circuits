package com.vayun.circuit.element.capacitor;

import com.vayun.circuit.element.CircuitElement;

public class Capacitor extends CircuitElement {

    private final double capacitance; // measured in farads

    private double charge; // measured in coulombs

    public Capacitor(double capacitance, double charge) {
        this.capacitance = capacitance;
        this.charge = charge;
    }

    @Override
    public void analyseVoltage(double voltage) {
        System.err.println("Cannot evaluate capacitor based on voltage");
        System.exit(1);
    }

    @Override
    public void analyseCurrent(double current) {
        this.setCurrent(current);
        this.setVoltage(charge / capacitance);
    }

    @Override
    public void update(double dt) {
        charge += getCurrent() * dt;
    }

    public double getCapacitance() {
        return capacitance;
    }

    public double getCharge() {
        return charge;
    }

    public String toString() {
        return super.toString() + String.format(" Q: %.2f C", charge);
    }
}