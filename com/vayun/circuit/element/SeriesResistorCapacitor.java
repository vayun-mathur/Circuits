package com.vayun.circuit.element;

public class SeriesResistorCapacitor extends ResistorCapacitor {

    private final ResistorCapacitor rc1, rc2;

    public SeriesResistorCapacitor(ResistorCapacitor rc1, ResistorCapacitor rc2) {
        super(rc1.getResistance()+rc2.getResistance(), inverse(rc1.getCapacitance(), rc2.getCapacitance()));
        this.rc1 = rc1;
        this.rc2 = rc2;
        this.setCharge(0);
    }

    public void analyseVoltage(double voltage) {
        super.analyseVoltage(voltage);
        rc1.analyseCurrent(getCurrent());
        rc2.analyseCurrent(getCurrent());
    }

    public void analyseCurrent(double current) {
        super.analyseCurrent(current);
        rc1.analyseCurrent(getCurrent());
        rc2.analyseCurrent(getCurrent());
    }

    @Override
    public void update(double dt) {
        rc1.update(dt);
        rc2.update(dt);
        this.setCharge(rc1.getCharge());
    }
}
