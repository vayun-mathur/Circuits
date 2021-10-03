package com.vayun.circuit.element;

public abstract class CircuitElement {

    private double voltage; // measured in volts
    private double current; // measured in amperes

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

    public static double inverse(double x, double y) {
        if(x==0) return y;
        if(y==0) return x;
        return x*y/(x+y);
    }

    public String toString() {
        return String.format("V: %.2f V, I: %.2f A", voltage, current);
    }

}
