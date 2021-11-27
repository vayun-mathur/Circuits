package com.vayun.circuit.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class DataTable {
    private final HashMap<String, DataColumn> columns = new HashMap<>();
    private final DataColumn time = new DataColumn("Time");

    public DataTable() {
        columns.put("Time", time);
    }

    public void update(double t) {
        time.addPoint(t, t);
    }

    public ObservableList<HashMap<String, Double>> getItems() {

        SortedMap<Double, HashMap<String, Double>> dataRows = new TreeMap<>();
        for(Double d: time.getValues().values()) {
            dataRows.put(d, new HashMap<>());
        }
        for(DataColumn col: columns.values()) {
            col.addToDataRows(dataRows);
        }
        return FXCollections.observableList(new ArrayList<>(dataRows.values()));
    }

    public ObservableList<DataColumn> columnsList() {
        return FXCollections.observableList(new ArrayList<>(columns.values()));
    }

    public void addPoint(double time, String column, double value) {
        columns.putIfAbsent(column, new DataColumn(column));
        columns.get(column).addPoint(time, value);
    }

    public List<DataPoint> getPoints(String x, String y) {
        DataColumn xCol = columns.get(x);
        DataColumn yCol = columns.get(y);
        List<DataPoint> points = new ArrayList<>();
        Set<Double> t = new HashSet<>(xCol.getValues().keySet());
        t.retainAll(yCol.getValues().keySet());
        for(Double d: t) {
            points.add(new DataPoint(xCol.getValues().get(d), yCol.getValues().get(d)));
        }
        return points;
    }
}
