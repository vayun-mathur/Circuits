package com.vayun.circuit.element;

import com.vayun.circuit.Circuit;

import java.util.ArrayList;
import java.util.List;

public class ResistorCapacitor extends CircuitElement {

    private final double resistance; // measured in ohms
    private final double capacitance; // measured in farads
    private double charge; // measured in coulombs

    public ResistorCapacitor(String name, double resistance, double capacitance) {
        super(name);
        this.resistance = resistance;
        this.capacitance = capacitance;
    }

    // Does not work if resistance is zero
    @Override
    public void analyseVoltage(double voltage) throws Exception {
        this.setVoltage(voltage);

        double vc = capacitance==0?0:charge / capacitance;
        double vr = voltage - vc;
        this.setCurrent(vr / resistance);
    }

    @Override
    public void analyseCurrent(double current) throws Exception {
        this.setCurrent(current);

        double vr = current * resistance;
        double vc = capacitance==0?0:charge / capacitance;
        this.setVoltage(vr + vc);
    }

    @Override
    public void update(double dt) {
        if(capacitance != 0) charge += getCurrent() * dt;
    }

    public double getResistance() {
        return resistance;
    }

    public double getCapacitance() {
        return capacitance;
    }

    public double getCharge() {
        return charge;
    }

    protected void setCharge(double charge) {
        this.charge = charge;
    }

    public String toString() {
        return super.toString() + String.format(" Q: %.2f C", charge);
    }

    @Override
    public List<Circuit.Connection> getConnections(List<CircuitElement> componentBefore, List<CircuitElement> componentAfter) {
        List<Circuit.Connection> conns = new ArrayList<>();
        componentBefore.forEach((x)->conns.add(new Circuit.Connection(x, this)));
        componentAfter.forEach((x)->conns.add(new Circuit.Connection(this, x)));
        return conns;
    }

    @Override
    public List<CircuitElement> getInNames() {
        return List.of(this);
    }

    @Override
    public List<CircuitElement> getOutNames() {
        return List.of(this);
    }
}
