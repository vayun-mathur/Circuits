package com.vayun.circuit.element;

import com.vayun.circuit.Circuit;

import java.util.ArrayList;
import java.util.List;

public class PowerSupply extends CircuitElement {

    public PowerSupply(String name, double voltage) {
        super(name);
        setVoltage(voltage);
    }

    @Override
    public void analyseVoltage(double voltage) {
        setVoltage(voltage);
    }

    @Override
    public void analyseCurrent(double current) {
        setCurrent(current);
    }

    @Override
    public void update(double dt) {

    }

    @Override
    public List<Circuit.Connection> getConnections(List<String> componentBefore, List<String> componentAfter) {
        List<Circuit.Connection> conns = new ArrayList<>();
        componentBefore.forEach((x)->conns.add(new Circuit.Connection(x, getName())));
        componentAfter.forEach((x)->conns.add(new Circuit.Connection(getName(), x)));
        return conns;
    }

    @Override
    public List<String> getInNames() {
        return List.of(getName());
    }

    @Override
    public List<String> getOutNames() {
        return List.of(getName());
    }
}
