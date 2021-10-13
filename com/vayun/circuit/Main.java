package com.vayun.circuit;

import com.vayun.circuit.element.ParallelResistorCapacitor;
import com.vayun.circuit.element.ResistorCapacitor;
import com.vayun.circuit.element.SeriesResistorCapacitor;
import com.vayun.circuit.gui.Controller;
import com.vayun.circuit.gui.Window;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ResistorCapacitor r1 = new ResistorCapacitor("R1", 6, 0);
        ResistorCapacitor c1 = new ResistorCapacitor("C1", 0, 3);
        ResistorCapacitor r2 = new ResistorCapacitor("R2", 1, 0);
        ResistorCapacitor c2 = new ResistorCapacitor("C2", 0, 0.5);
        ResistorCapacitor c3 = new ResistorCapacitor("C3", 0, 0.9);
        SeriesResistorCapacitor rc1 = new SeriesResistorCapacitor(c1, r1);
        SeriesResistorCapacitor rc2 = new SeriesResistorCapacitor(new SeriesResistorCapacitor(c2, c3), r2);
        Circuit c = new Circuit(12, new ParallelResistorCapacitor(rc1, rc2), List.of(r1, c1, r2, c2, c3));
        c.analyse();
        Controller.circuit = c;
        Window.run();
    }
}
