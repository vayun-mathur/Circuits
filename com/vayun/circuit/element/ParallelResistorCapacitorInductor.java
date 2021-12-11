package com.vayun.circuit.element;

import com.vayun.circuit.Circuit;
import com.vayun.circuit.data.DataTable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelResistorCapacitorInductor extends ResistorCapacitorInductor {

    private final ResistorCapacitorInductor rc1, rc2;

    public ParallelResistorCapacitorInductor(ResistorCapacitorInductor rc1, ResistorCapacitorInductor rc2) {
        super("", inverse(rc1.getResistanceForward(), rc2.getResistanceForward()),
                inverse(rc1.getResistanceBackward(), rc2.getResistanceBackward()),
                rc1.getCapacitance() + rc2.getCapacitance(),
                inverse(rc1.getInductance(), rc2.getInductance()));
        this.rc1 = rc1;
        this.rc2 = rc2;
        this.setCharge(rc1.getCharge()+rc2.getCharge());
    }

    public void analyseVoltage(double voltage) throws Exception {
        rc1.analyseVoltage(voltage);
        rc2.analyseVoltage(voltage);
        setVoltage(voltage);
        setCurrent(rc1.getCurrent()+rc2.getCurrent());
    }

    public void analyseCurrent(double current) throws Exception {
        super.analyseCurrent(current);
        rc1.analyseVoltage(getVoltage());
        rc2.analyseVoltage(getVoltage());
    }

    @Override
    public void update(double dt, double t, DataTable dtable) {
        rc1.update(dt, t, dtable);
        rc2.update(dt, t, dtable);
        this.setCharge(rc1.getCharge()+rc2.getCharge());
    }

    @Override
    public List<Circuit.Connection> getConnections(List<CircuitElement> componentBefore, List<CircuitElement> componentAfter) {
        return Stream.concat(
                rc1.getConnections(componentBefore, componentAfter).stream(),
                rc2.getConnections(componentBefore, componentAfter).stream()
        ).collect(Collectors.toList());
    }

    @Override
    public List<CircuitElement> getInNames() {
        return Stream.concat(
                rc1.getInNames().stream(),
                rc2.getInNames().stream()
        ).collect(Collectors.toList());
    }

    @Override
    public List<CircuitElement> getOutNames() {
        return Stream.concat(
                rc1.getOutNames().stream(),
                rc2.getOutNames().stream()
        ).collect(Collectors.toList());
    }
}
