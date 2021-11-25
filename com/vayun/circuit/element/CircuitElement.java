package com.vayun.circuit.element;

import com.vayun.circuit.Circuit;
import com.vayun.circuit.data.DataTable;

import java.util.List;

public abstract class CircuitElement {

    private final String name;

    private double voltage; // measured in volts
    private double current; // measured in amperes

    public CircuitElement(String name) {
        this.name = name;
    }

    public abstract void analyseVoltage(double voltage) throws Exception;
    public abstract void analyseCurrent(double current) throws Exception;
    public void update(double dt, double t, DataTable dtable) {
        dtable.addPoint(t, name+":V", getVoltage());
        dtable.addPoint(t, name+":I", getCurrent());
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

    public abstract List<Circuit.Connection> getConnections(List<CircuitElement> componentBefore, List<CircuitElement> componentAfter);

    public abstract List<CircuitElement> getInNames();
    public abstract List<CircuitElement> getOutNames();
}
