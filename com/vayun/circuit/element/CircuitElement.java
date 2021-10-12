package com.vayun.circuit.element;

import com.vayun.circuit.Circuit;

import java.util.List;

public abstract class CircuitElement {

    private final String name;

    private double voltage; // measured in volts
    private double current; // measured in amperes

    public CircuitElement(String name) {
        this.name = name;
    }

    public abstract void analyseVoltage(double voltage);
    public abstract void analyseCurrent(double current);
    public abstract void update(double dt);

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

    public String getName() {
        return name;
    }

    public static double inverse(double x, double y) {
        if(x==0) return y;
        if(y==0) return x;
        return x*y/(x+y);
    }

    public String toString() {
        return String.format("V: %.2f V, I: %.2f A", voltage, current);
    }

    public abstract List<Circuit.Connection> getConnections(List<String> componentBefore, List<String> componentAfter);

    public abstract List<String> getInNames();
    public abstract List<String> getOutNames();
}
