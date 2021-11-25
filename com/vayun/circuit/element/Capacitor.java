package com.vayun.circuit.element;

import com.vayun.circuit.data.DataTable;

public class Capacitor extends ResistorCapacitorInductor {
    public Capacitor(String name, double capacitance) {
        super(name, 0, capacitance, 0);
    }

    @Override
    public void update(double dt, double t, DataTable dtable) {
        dtable.addPoint(t, getName()+":Q", getCharge());
        super.update(dt, t, dtable);
    }
}
