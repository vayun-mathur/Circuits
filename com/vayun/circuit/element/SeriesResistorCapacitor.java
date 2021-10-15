package com.vayun.circuit.element;

import com.vayun.circuit.Circuit;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SeriesResistorCapacitor extends ResistorCapacitor {

    private final ResistorCapacitor rc1, rc2;

    public SeriesResistorCapacitor(ResistorCapacitor rc1, ResistorCapacitor rc2) {
        super("",rc1.getResistance()+rc2.getResistance(), inverse(rc1.getCapacitance(), rc2.getCapacitance()));
        this.rc1 = rc1;
        this.rc2 = rc2;
        this.setCharge(0);
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
    public void update(double dt) {
        rc1.update(dt);
        rc2.update(dt);
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
