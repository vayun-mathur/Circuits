package com.vayun.circuit;

import com.vayun.circuit.element.ParallelResistorCapacitor;
import com.vayun.circuit.element.ResistorCapacitor;
import com.vayun.circuit.element.SeriesResistorCapacitor;

public class Main {
    public static void main(String[] args) {
        ResistorCapacitor r1 = new ResistorCapacitor(6, 0);
        ResistorCapacitor c1 = new ResistorCapacitor(0, 3);
        ResistorCapacitor r2 = new ResistorCapacitor(1, 0);
        ResistorCapacitor c2 = new ResistorCapacitor(0, 0.5);
        ResistorCapacitor c3 = new ResistorCapacitor(0, 0.9);
        SeriesResistorCapacitor rc1 = new SeriesResistorCapacitor(c1, r1);
        SeriesResistorCapacitor rc2 = new SeriesResistorCapacitor(new SeriesResistorCapacitor(c2, c3), r2);
        Circuit c = new Circuit(12, new ParallelResistorCapacitor(rc1, rc2));
        for(int i=0;i<5000;i++) {
            c.analyse();
            c.update(0.02);
        }
        System.out.println(r1);
        System.out.println(c1);
        System.out.println(r2);
        System.out.println(c2);
        System.out.println(c3);
    }
}
