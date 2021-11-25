package com.vayun.circuit.element;

import com.vayun.circuit.Circuit;
import com.vayun.circuit.data.DataTable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SeriesResistorCapacitorInductor extends ResistorCapacitorInductor {

    private final ResistorCapacitorInductor rc1, rc2;

    public SeriesResistorCapacitorInductor(ResistorCapacitorInductor rc1, ResistorCapacitorInductor rc2) {
        super("",rc1.getResistance()+rc2.getResistance(), inverse(rc1.getCapacitance(), rc2.getCapacitance()), rc1.getInductance()+rc2.getInductance());
        this.rc1 = rc1;
        this.rc2 = rc2;
        this.setCharge(Math.max(rc1.getCharge(), rc2.getCharge()));
    }

    public void analyseVoltage(double voltage) throws Exception {
        super.analyseVoltage(voltage);
        rc1.analyseCurrent(getCurrent());
        rc2.analyseCurrent(getCurrent());
    }

    public void analyseCurrent(double current) throws Exception {
        rc1.analyseCurrent(current);
        rc2.analyseCurrent(current);
        setCurrent(current);
        setVoltage(rc1.getVoltage()+rc2.getVoltage());
    }

    @Override
    public void update(double dt, double t, DataTable dtable) {
        rc1.update(dt, t, dtable);
        rc2.update(dt, t, dtable);
        this.setCharge(Math.max(rc1.getCharge(), rc2.getCharge()));
    }

    @Override
    public List<Circuit.Connection> getConnections(List<CircuitElement> componentBefore, List<CircuitElement> componentAfter) {
        return Stream.concat(
                    rc1.getConnections(componentBefore, rc2.getInNames()).stream(),
                    rc2.getConnections(rc1.getOutNames(), componentAfter).stream()
                ).collect(Collectors.toList());
    }

    @Override
    public List<CircuitElement> getInNames() {
        return rc1.getInNames();
    }

    @Override
    public List<CircuitElement> getOutNames() {
        return rc2.getOutNames();
    }
}
