package com.vayun.circuit;

import com.vayun.circuit.element.SeriesResistorCapacitor;
import com.vayun.circuit.element.capacitor.Capacitor;
import com.vayun.circuit.element.capacitor.ParallelCapacitor;
import com.vayun.circuit.element.capacitor.SeriesCapacitor;
import com.vayun.circuit.element.resistor.Resistor;

public class Main {
    public static void main(String[] args) {
        Resistor r1 = new Resistor(6);
        Capacitor c1 = new Capacitor(3, 3);
        Circuit c = new Circuit(12, new SeriesResistorCapacitor(c1, r1));
        for(int i=0;i<5000;i++) {
            c.analyse();
            c.update(0.02);
        }
        System.out.println(r1);
        System.out.println(c1);
    }
}
