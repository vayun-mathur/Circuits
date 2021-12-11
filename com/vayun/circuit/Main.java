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
        Diode d0 = new Diode("D0");
        SeriesResistorCapacitorInductor rc1 = new SeriesResistorCapacitorInductor(c1, r1);
        SeriesResistorCapacitorInductor rc2 = new SeriesResistorCapacitorInductor(new SeriesResistorCapacitorInductor(d0, new SeriesResistorCapacitorInductor(c2, c3)), r2);
        ParallelResistorCapacitorInductor cir = new ParallelResistorCapacitorInductor(rc1, rc2);
        SeriesResistorCapacitorInductor nodes = new SeriesResistorCapacitorInductor(new SeriesResistorCapacitorInductor(n0, cir), n1);
        Circuit c = new Circuit(new PowerSupply("B", 12), nodes, List.of(r1, c1, r2, c2, c3, n0, n1, d0));
        c.analyse();
        Controller.circuit = c;
        Window.run();
    }
}
