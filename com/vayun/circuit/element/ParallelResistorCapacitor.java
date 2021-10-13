package com.vayun.circuit.element;

import com.vayun.circuit.Circuit;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelResistorCapacitor extends ResistorCapacitor {

    private final ResistorCapacitor rc1, rc2;

    public ParallelResistorCapacitor(ResistorCapacitor rc1, ResistorCapacitor rc2) {
        super("", inverse(rc1.getResistance(), rc2.getResistance()), rc1.getCapacitance() + rc2.getCapacitance());
        this.rc1 = rc1;
        this.rc2 = rc2;
        this.setCharge(0);
    }

    public void analyseVoltage(double voltage) {
        rc1.analyseVoltage(voltage);
        rc2.analyseVoltage(voltage);
        setVoltage(voltage);
        setCurrent(rc1.getCurrent()+rc2.getCurrent());
    }

    public void analyseCurrent(double current) {
        super.analyseCurrent(current);
        rc1.analyseVoltage(getVoltage());
        rc2.analyseVoltage(getVoltage());
    }

    @Override
    public void update(double dt) {
        rc1.update(dt);
        rc2.update(dt);
        this.setCharge(rc1.getCharge()+rc2.getCharge());
    }

    @Override
    public List<Circuit.Connection> getConnections(List<String> componentBefore, List<String> componentAfter) {
        return Stream.concat(
                rc1.getConnections(componentBefore, componentAfter).stream(),
                rc2.getConnections(componentBefore, componentAfter).stream()
        ).collect(Collectors.toList());
    }

    @Override
    public List<String> getInNames() {
        return Stream.concat(
                rc1.getInNames().stream(),
                rc2.getInNames().stream()
        ).collect(Collectors.toList());
    }

    @Override
    public List<String> getOutNames() {
        return Stream.concat(
                rc1.getOutNames().stream(),
                rc2.getOutNames().stream()
        ).collect(Collectors.toList());
    }
}
