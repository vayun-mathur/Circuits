package com.vayun.circuit.element;

public class ParallelResistorCapacitor extends ResistorCapacitor {

    private final ResistorCapacitor rc1, rc2;

    public ParallelResistorCapacitor(ResistorCapacitor rc1, ResistorCapacitor rc2) {
        super(inverse(rc1.getResistance(), rc2.getResistance()), rc1.getCapacitance() + rc2.getCapacitance());
        this.rc1 = rc1;
        this.rc2 = rc2;
        this.setCharge(0);
    }

    public void analyseVoltage(double voltage) {
        super.analyseVoltage(voltage);
        rc1.analyseVoltage(getVoltage());
        rc2.analyseVoltage(getVoltage());
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
}
