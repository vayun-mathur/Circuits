package com.vayun.circuit.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataColumn {

    private final String name;
    private final List<Double> values = new ArrayList<>();

    public DataColumn(String name) {
        this.name = name;
    }

    public void addPoint(double d) {
        values.add(d);
    }

    public String getName() {
        return name;
    }

    public List<Double> getValues() {
        return values;
    }

    public String toString() {
        return name;
    }

    public void addToDataRows(List<HashMap<String, Double>> dataRows) {
        for(int i=0;i<dataRows.size();i++) {
            dataRows.get(i).put(name, values.get(i));
        }
    }
}
