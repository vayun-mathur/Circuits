package com.vayun.circuit;

import com.vayun.circuit.resistor.ParallelResistor;
import com.vayun.circuit.resistor.Resistor;
import com.vayun.circuit.resistor.SeriesResistor;

public class Main {
    public static void main(String[] args) {
        Resistor r1 = new Resistor(6);
        Resistor r2 = new Resistor(3);
        Resistor r3 = new Resistor(1);
        Circuit c = new Circuit(12, new ParallelResistor(r1, new SeriesResistor(r2, r3)));
        c.analyse();
        System.out.println(r1.getCurrent());
        System.out.println(r2.getCurrent());
        System.out.println(r3.getCurrent());
        System.out.println(r1.getVoltage());
        System.out.println(r2.getVoltage());
        System.out.println(r3.getVoltage());
    }
}
