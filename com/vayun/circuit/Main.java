package com.vayun.circuit;

import com.vayun.circuit.element.*;
import com.vayun.circuit.gui.Controller;
import com.vayun.circuit.gui.Window;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Resistor r1 = new Resistor("R1", 6);
        Capacitor c1 = new Capacitor("C1", 3);
        Resistor r2 = new Resistor("R2", 1);
        Capacitor c2 = new Capacitor("C2", 0.5);
        Capacitor c3 = new Capacitor("C3", 0.9);
        Node n0 = new Node("N0");
        Node n1 = new Node("N1");
        SeriesResistorCapacitor rc1 = new SeriesResistorCapacitor(c1, r1);
        SeriesResistorCapacitor rc2 = new SeriesResistorCapacitor(new SeriesResistorCapacitor(c2, c3), r2);
        ParallelResistorCapacitor cir = new ParallelResistorCapacitor(rc1, rc2);
        SeriesResistorCapacitor nodes = new SeriesResistorCapacitor(new SeriesResistorCapacitor(n0, cir), n1);
        Circuit c = new Circuit(new PowerSupply("B", 12), nodes, List.of(r1, c1, r2, c2, c3, n0, n1));
        c.analyse();
        Controller.circuit = c;
        Window.run();
    }
}
