package com.vayun.circuit.data.equation;

import com.vayun.circuit.data.DataPoint;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Equation {
    public static double[] linearParameters(List<DataPoint> points) {
        List<Double> x = points.stream().map(p->p.x).collect(Collectors.toList());
        List<Double> y = points.stream().map(p->p.y).collect(Collectors.toList());
        List<Double> xy = points.stream().map(p->p.x*p.y).collect(Collectors.toList());
        double xavg = average(x), yavg = average(y), xyavg = average(xy);
        double x2avg = average(x.stream().map(n->n*n).collect(Collectors.toList()));
        double y2avg = average(y.stream().map(n->n*n).collect(Collectors.toList()));

        double r = (xyavg - xavg*yavg)/Math.sqrt((x2avg-xavg*xavg)*(y2avg-yavg*yavg));
        double b = r * standardDeviation(y)/standardDeviation(x);
        double a = average(y) - (b * average(x));
        return new double[]{a, b, r*r};
    }

    public static Equation linearRegression(List<DataPoint> points) {
        double[] abr = linearParameters(points);
        return new LinearEquation(round(abr[0]), round(abr[1]), round(abr[2]));
    }

    public static Equation inverseRegression(List<DataPoint> points) {
        double[] abr = linearParameters(points.stream().filter(p->p.x!=0).map(p->new DataPoint(1/p.x, p.y)).collect(Collectors.toList()));
        return new InverseEquation(round(abr[0]), round(abr[1]), round(abr[2]));
    }

    private static double round(double x) {
        return Math.round(x*1000)/1000.0;
    }

    private static double average(List<Double> d) {
        double sum = 0;
        for(Double a: d) sum += a;
        return sum/d.size();
    }

    private static double standardDeviation(List<Double> d) {
        double diff = 0;
        double avg = average(d);
        for(Double a: d) diff += (a-avg)*(a-avg);
        return Math.sqrt(diff/d.size());
    }
}

class LinearEquation extends Equation {
    private final double a, b, r2;

    public LinearEquation(double a, double b, double r2) {
        this.a = a;
        this.b = b;
        this.r2 = r2;
    }

    public String toString() {
        return "y = " + a + " + " + b + "x, r²="+r2;
    }
}

class InverseEquation extends Equation {
    private final double a, b, r2;

    public InverseEquation(double a, double b, double r2) {
        this.a = a;
        this.b = b;
        this.r2 = r2;
    }

    public String toString() {
        return "y = " + a + " + " + b + " * 1/x, r²="+r2;
    }
}