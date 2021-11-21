package com.vayun.circuit.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

public class DataColumn {

    private final String name;
    private final HashMap<Double, Double> values = new HashMap<>();

    public DataColumn(String name) {
        this.name = name;
    }

    public void addPoint(double t, double v) {
        values.put(t, v);
    }

    public String getName() {
        return name;
    }

    public HashMap<Double, Double> getValues() {
        return values;
    }

    public String toString() {
        return name;
    }

    public void addToDataRows(SortedMap<Double, HashMap<String, Double>> dataRows) {
        for(Double i: values.keySet()) {
            dataRows.get(i).put(name, values.get(i));
        }
    }
}
