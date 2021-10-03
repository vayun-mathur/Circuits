package com.vayun.circuit.resistor;

public class Resistor {

    // Inputted values
    private final double resistance; // measured in ohms

    // Calculated values
    private double voltage; // measured in volts
    private double current; // measured in amperes

    public Resistor(double resistance) {
        this.resistance = resistance;
    }

    public void analyseVoltage(double voltage) {
        this.voltage = voltage;
        this.current = voltage / resistance;
    }

    public void analyseCurrent(double current) {
        this.current = current;
        this.voltage = current * resistance;
    }

    public double getResistance() {
        return resistance;
    }

    public double getVoltage() {
        return voltage;
    }

    protected void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public double getCurrent() {
        return current;
    }

    protected void setCurrent(double current) {
        this.current = current;
    }
}
