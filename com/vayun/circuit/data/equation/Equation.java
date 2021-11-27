package com.vayun.circuit.data.equation;

import com.vayun.circuit.data.DataPoint;

import java.util.List;
import java.util.stream.Collectors;

public class Equation {
    public static Equation linearRegression(List<DataPoint> points) {
        List<Double> x = points.stream().map(p->p.x).collect(Collectors.toList());
        List<Double> y = points.stream().map(p->p.y).collect(Collectors.toList());

        double b1 = standardDeviation(y)/standardDeviation(x);
        double b0 = average(y) - (b1 * average(x));
        return new LinearEquation(round(b0), round(b1));
    }

    private static double round(double x) {
        return Math.round(x*1000)/1000.0;
    }

    public static double average(List<Double> d) {
        double sum = 0;
        for(Double a: d) sum += a;
        return sum/d.size();
    }

    public static double standardDeviation(List<Double> d) {
        double diff = 0;
        double avg = average(d);
        for(Double a: d) diff += (a-avg)*(a-avg);
        return Math.sqrt(diff/d.size());
    }
}

class LinearEquation extends Equation {
    private double b0, b1;

    public LinearEquation(double b0, double b1) {
        this.b0 = b0;
        this.b1 = b1;
    }

    public double getB0() {
        return b0;
    }

    public double getB1() {
        return b1;
    }

    public String toString() {
        return "y = " + b0 + " + " + b1 + "x";
    }
}