package com.vayun.circuit;

import com.vayun.circuit.resistor.ParallelResistor;
import com.vayun.circuit.resistor.Resistor;

public class Main {
    public static void main(String[] args) {
        Resistor r1 = new Resistor(6);
        Resistor r2 = new Resistor(4);
        Circuit c = new Circuit(12, new ParallelResistor(r1, r2));
        c.analyse();
        System.out.println(r1.getCurrent());
        System.out.println(r2.getCurrent());
    }
}
