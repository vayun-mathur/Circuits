package com.vayun.circuit;

import com.vayun.circuit.element.CircuitElement;
import com.vayun.circuit.element.ResistorCapacitor;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Circuit {

    private final double voltage;

    private final CircuitElement circuit;

    private final Map<String, CircuitElement> elements;

    public Circuit(double voltage, CircuitElement circuit, List<CircuitElement> elements) {
        this.voltage = voltage;
        this.circuit = circuit;
        this.elements = new HashMap<>();
        for(CircuitElement e: elements) {
            this.elements.put(e.getName(), e);
        }
    }

    public void analyse() {
        this.circuit.analyseVoltage(voltage);
    }

    public void update(double dt) {
        this.circuit.update(dt);
    }

    public CircuitElement getElement(String name) {
        return elements.get(name);
    }

    public Collection<CircuitElement> getElements() {
        return elements.values();
    }

    public static class Connection {
        public String component1, component2;

        public Connection(String component1, String component2) {
            this.component1 = component1;
            this.component2 = component2;
        }
    }

    public List<Connection> getConnections() {
        return circuit.getConnections(List.of("B"), List.of("B"));
    }
}
