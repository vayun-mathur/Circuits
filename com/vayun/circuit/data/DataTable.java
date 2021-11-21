package com.vayun.circuit.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class DataTable {
    private final HashMap<String, DataColumn> columns = new HashMap<>();
    private final DataColumn time = new DataColumn("time");

    public DataTable() {
        columns.put("time", time);
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
}
