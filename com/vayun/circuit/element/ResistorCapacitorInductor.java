package com.vayun.circuit.element;

import com.vayun.circuit.Circuit;

import java.util.ArrayList;
import java.util.List;

public class ResistorCapacitorInductor extends CircuitElement {

    private final double resistance; // measured in ohms
    private final double capacitance; // measured in farads
    private final double inductance; // measured in henries
    private double charge; // measured in coulombs

    public ResistorCapacitorInductor(String name, double resistance, double capacitance, double inductance) {
        super(name);
        this.resistance = resistance;
        this.capacitance = capacitance;
        this.inductance = inductance;
    }

    // Does not work if resistance is zero
    @Override
    public void analyseVoltage(double voltage) throws Exception {
        double old_current = this.getCurrent();
        this.setVoltage(voltage);

        double vc = capacitance==0?0:charge / capacitance;
        double vr = (voltage - vc + inductance * old_current / dt) * (resistance * dt)/(resistance * dt + inductance);
        this.setCurrent(vr / resistance);
    }

    @Override
    public void analyseCurrent(double current) throws Exception {
        double old_current = this.getCurrent();
        this.setCurrent(current);
        double vi = inductance * (current-old_current)/dt;
        double vr = current * resistance;
        double vc = capacitance==0?0:charge / capacitance;
        this.setVoltage(vr + vc + vi);
    }

    double dt = 0.02;

    @Override
    public void update(double dt) {
        if(capacitance != 0) charge += getCurrent() * dt;
        this.dt = dt;
    }

    public double getResistance() {
        return resistance;
    }

    public double getCapacitance() {
        return capacitance;
    }

    public double getInductance() {
        return inductance;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
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
