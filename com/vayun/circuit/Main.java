package com.vayun.circuit;

import com.vayun.circuit.resistor.Resistor;

public class Main {
    public static void main(String[] args) {
        Resistor r = new Resistor(1);
        Circuit c = new Circuit(12, r);
        c.analyse();
        System.out.println(r.getCurrent());
    }
}
