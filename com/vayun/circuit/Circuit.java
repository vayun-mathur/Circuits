package com.vayun.circuit;

import com.vayun.circuit.element.CircuitElement;
import com.vayun.circuit.element.PowerSupply;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Circuit {

    private final PowerSupply supply;

    private final CircuitElement circuit;

    private final Map<String, CircuitElement> elements;

    public Circuit(PowerSupply supply, CircuitElement circuit, List<CircuitElement> elements) {
        this.supply = supply;
        this.circuit = circuit;
        this.elements = new HashMap<>();
        for(CircuitElement e: elements) {
            this.elements.put(e.getName(), e);
        }
        this.elements.put(supply.getName(), supply);
    }

    public void analyse() throws Exception {
        this.circuit.analyseVoltage(supply.getVoltage());
        this.supply.analyseCurrent(this.circuit.getCurrent());
    }

    public void update(double dt) {
        this.circuit.update(dt);
        this.supply.update(dt);
    }

    public CircuitElement getElement(String name) {
        return elements.get(name);
    }

    public Collection<CircuitElement> getElements() {
        return elements.values();
    }

    public void addElement(CircuitElement e) {
        this.elements.put(e.getName(), e);
    }

    public void removeElement(String name) {
        this.elements.remove(name);
    }

    public static class Connection {
        public CircuitElement component1, component2;

        public Connection(CircuitElement component1, CircuitElement component2) {
            this.component1 = component1;
            this.component2 = component2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Connection that = (Connection) o;
            return Objects.equals(component1, that.component1) &&
                    Objects.equals(component2, that.component2);
        }
    }

    public List<Connection> getConnections() {
        return circuit.getConnections(supply.getInNames(), supply.getOutNames()).stream().filter( distinctByKey(p -> p.component1.getName()+p.component2.getName()) ).collect(Collectors.toList());
    }

    //Utility function
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
