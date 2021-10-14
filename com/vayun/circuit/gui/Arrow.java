package com.vayun.circuit.gui;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.scene.Group;
import javafx.scene.shape.Line;

public class Arrow extends Group {

    private final Line line;

    private ElementGUI c1, c2;

    private Arrow() {
        this(new Line(), new Line(), new Line());
    }

    private static final double arrowLength = 20;
    private static final double arrowWidth = 7;

    private static DoubleBinding cos(ObservableNumberValue other) {
        return new DoubleBinding() {
            {
                bind(other);
            }
            @Override
            protected double computeValue() {
                return Math.cos(other.doubleValue());
            }
        };
    }

    private static DoubleBinding sin(ObservableNumberValue other) {
        return new DoubleBinding() {
            {
                bind(other);
            }
            @Override
            protected double computeValue() {
                return Math.sin(other.doubleValue());
            }
        };
    }

    private static DoubleBinding atan2(ObservableNumberValue y, ObservableNumberValue x) {
        return new DoubleBinding() {
            {
                bind(y);
                bind(x);
            }
            @Override
            protected double computeValue() {
                return Math.atan2(y.doubleValue(), x.doubleValue());
            }
        };
    }

    public Arrow(ElementGUI c1, ElementGUI c2) {
        this();
        this.c1 = c1;
        this.c2 = c2;

        var angle = atan2(c2.centerYProperty().subtract(c1.centerYProperty()), c2.centerXProperty().subtract(c1.centerXProperty()));

        this.startXProperty().bind(c1.translateXProperty().add(c1.widthProperty().divide(2)).add(
                c1.widthProperty().divide(2).multiply(cos(angle))
        ));

        this.startYProperty().bind(c1.translateYProperty().add(c1.heightProperty().divide(2)).add(
                c1.heightProperty().divide(2).multiply(sin(angle))
        ));

        this.endXProperty().bind(c2.translateXProperty().add(c2.widthProperty().divide(2)).subtract(
                c2.widthProperty().divide(2).multiply(cos(angle))
        ));

        this.endYProperty().bind(c2.translateYProperty().add(c2.heightProperty().divide(2)).subtract(
                c2.heightProperty().divide(2).multiply(sin(angle))
        ));
    }

    public ElementGUI getC1() {
        return c1;
    }

    public ElementGUI getC2() {
        return c2;
    }

    private Arrow(Line line, Line arrow1, Line arrow2) {
        super(line, arrow1, arrow2);
        this.line = line;
        InvalidationListener updater = o -> {
            double ex = getEndX();
            double ey = getEndY();
            double sx = getStartX();
            double sy = getStartY();

            double mx = (sx + ex)/2;
            double my = (sy + ey)/2;

            arrow1.setEndX(mx);
            arrow1.setEndY(my);
            arrow2.setEndX(mx);
            arrow2.setEndY(my);

            if (ex == sx && ey == sy) {
                // arrow parts of length 0
                arrow1.setStartX(mx);
                arrow1.setStartY(my);
                arrow2.setStartX(mx);
                arrow2.setStartY(my);
            } else {
                double factor = arrowLength / Math.hypot(sx-ex, sy-ey);
                double factorO = arrowWidth / Math.hypot(sx-ex, sy-ey);

                // part in direction of main line
                double dx = (sx - ex) * factor;
                double dy = (sy - ey) * factor;

                // part ortogonal to main line
                double ox = (sx - ex) * factorO;
                double oy = (sy - ey) * factorO;

                arrow1.setStartX(mx + dx - oy);
                arrow1.setStartY(my + dy + ox);
                arrow2.setStartX(mx + dx + oy);
                arrow2.setStartY(my + dy - ox);
            }
        };

        // add updater to properties
        startXProperty().addListener(updater);
        startYProperty().addListener(updater);
        endXProperty().addListener(updater);
        endYProperty().addListener(updater);
        updater.invalidated(null);
    }

    // start/end properties

    public final void setStartX(double value) {
        line.setStartX(value);
    }

    public final double getStartX() {
        return line.getStartX();
    }

    public final DoubleProperty startXProperty() {
        return line.startXProperty();
    }

    public final void setStartY(double value) {
        line.setStartY(value);
    }

    public final double getStartY() {
        return line.getStartY();
    }

    public final DoubleProperty startYProperty() {
        return line.startYProperty();
    }

    public final void setEndX(double value) {
        line.setEndX(value);
    }

    public final double getEndX() {
        return line.getEndX();
    }

    public final DoubleProperty endXProperty() {
        return line.endXProperty();
    }

    public final void setEndY(double value) {
        line.setEndY(value);
    }

    public final double getEndY() {
        return line.getEndY();
    }

    public final DoubleProperty endYProperty() {
        return line.endYProperty();
    }

    public String getName() {
        return c1.getName()+c2.getName();
    }
}