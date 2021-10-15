package com.vayun.circuit.gui;

import com.vayun.circuit.Circuit;
import com.vayun.circuit.element.*;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.swing.text.Element;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Controller {

    public static Circuit circuit;

    public volatile CircuitElement currSelected;

    @FXML
    private Pane resistorPane;
    @FXML
    private Label resistorName;
    @FXML
    private Label resistorVoltage;
    @FXML
    private Label resistorCurrent;

    @FXML
    private Pane capacitorPane;
    @FXML
    private Label capacitorName;
    @FXML
    private Label capacitorVoltage;
    @FXML
    private Label capacitorCharge;

    @FXML
    private ListView<String> componentsList;

    @FXML
    private Label leftstatus;

    @FXML
    private Pane circuitCanvas;

    @FXML
    private VBox mainPane;

    private double t = 0;
    private boolean play = false;

    DoubleProperty rotation;

    public void initialize() {
        mainPane.requestFocus();
        for (CircuitElement e : circuit.getElements()) {
            if (!(e instanceof Node))
                componentsList.getItems().add(e.getName());
        }
        componentsList.getItems().sort(String::compareTo);

        componentsList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> currSelected = circuit.getElement(newValue));

        currSelected = circuit.getElement("B");
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Controller.this.handle();
            }
        };

        mainPane.setOnKeyPressed((t) -> {
            for (ElementGUI p : elements_screen.values()) {
                p.getOnKeyPressed().handle(t);
            }
            if (t.getCode() == KeyCode.N && !play) {
                int nNumber = 0;
                while (elements_screen.containsKey("N" + nNumber)) nNumber++;
                addElement(new Node("N" + nNumber));
            }
            if (t.getCode() == KeyCode.C && !play) {
                int nNumber = 0;
                while (elements_screen.containsKey("C" + nNumber)) nNumber++;
                addElement(new Capacitor("C" + nNumber, 1));
            }
            if (t.getCode() == KeyCode.R && !play) {
                int nNumber = 0;
                while (elements_screen.containsKey("R" + nNumber)) nNumber++;
                addElement(new Resistor("R" + nNumber, 1));
            }
        });
        mainPane.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (event.getCharacter().equals(" ")) {
                play = !play;
                if (play) {
                    circuit = createCircuit();
                }
            }
        });

        drawCircuit();
        handle();
        leftstatus.setText("t = 0.00 seconds");

        timer.start();
    }

    public void handle() {

        try {
            circuit.analyse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (play) {
            circuit.update(0.02);
            t += 0.02;
            leftstatus.setText(String.format("t = %.2f seconds", t));
        }

        if (currSelected instanceof Resistor) {
            setResistor((Resistor) currSelected);
        }
        if (currSelected instanceof Capacitor) {
            setCapacitor((Capacitor) currSelected);
        }
        if (currSelected instanceof PowerSupply) {
            setPowerSupply((PowerSupply) currSelected);
        }


        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addElement(CircuitElement e) {
        if (play) return;
        elements_screen.put(e.getName(), new ElementGUI(e.getName(), 50, 50, e, this));
        circuitCanvas.getChildren().add(elements_screen.get(e.getName()));
        circuit.addElement(e);
    }

    public void removeElement(String name) {
        if (play) return;
        while (!elements_screen.get(name).getConnections().isEmpty()) {
            removeArrow(elements_screen.get(name).getConnections().get(0));
        }
        circuitCanvas.getChildren().remove(elements_screen.get(name));
        elements_screen.remove(name);
        currSelected = null;
    }

    public void addArrow(Arrow arrow) {
        if (play) return;
        arrow.getC1().getConnections().add(arrow);
        arrow.getC2().getConnections().add(arrow);
        arrow_screen.put(arrow.getName(), arrow);
        circuitCanvas.getChildren().add(arrow);
    }

    public void removeArrow(Arrow arrow) {
        if (play) return;
        arrow.getC1().getConnections().remove(arrow);
        arrow.getC2().getConnections().remove(arrow);
        arrow_screen.remove(arrow.getName());
        circuitCanvas.getChildren().remove(arrow);
    }

    private final ConcurrentHashMap<String, ElementGUI> elements_screen = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Arrow> arrow_screen = new ConcurrentHashMap<>();

    public void drawCircuit() {
        circuitCanvas.getChildren().clear();
        int c = 0;
        for (CircuitElement e : circuit.getElements()) {
            elements_screen.put(e.getName(), new ElementGUI(e.getName(), 50 + c * 30, 100, e, this));
            c++;
        }
        circuitCanvas.getChildren().addAll(elements_screen.values());
        for (Circuit.Connection conn : circuit.getConnections()) {
            Arrow a = new Arrow(elements_screen.get(conn.component1.getName()), elements_screen.get(conn.component2.getName()));
            arrow_screen.put(conn.component1.getName() + conn.component2.getName(), a);
            elements_screen.get(conn.component1.getName()).getConnections().add(a);
            elements_screen.get(conn.component2.getName()).getConnections().add(a);
            circuitCanvas.getChildren().add(a);
        }
        elements_screen.values().forEach(ElementGUI::toFront);
    }

    double orgSceneX, orgSceneY;

    public void setCapacitor(Capacitor c) {
        resistorPane.setVisible(false);
        capacitorPane.setVisible(true);
        capacitorName.setText("Component: " + c.getName());
        capacitorVoltage.setText(String.format("Voltage: %.3fV", c.getVoltage()));
        capacitorCharge.setText(String.format("Charge: %.3fC", c.getCharge()));
        rotation = elements_screen.get(c.getName()).rotateProperty();
    }

    public void setResistor(Resistor r) {
        resistorPane.setVisible(true);
        capacitorPane.setVisible(false);
        resistorName.setText("Component: " + r.getName());
        resistorVoltage.setText(String.format("Voltage: %.3fV", r.getVoltage()));
        resistorCurrent.setText(String.format("Current: %.3fA", r.getCurrent()));
        rotation = elements_screen.get(r.getName()).rotateProperty();
    }

    private void setPowerSupply(PowerSupply p) {
        resistorPane.setVisible(true);
        capacitorPane.setVisible(false);
        resistorName.setText("Component: " + p.getName());
        resistorVoltage.setText(String.format("Voltage: %.3fV", p.getVoltage()));
        resistorCurrent.setText(String.format("Current: %.3fA", p.getCurrent()));
        rotation = elements_screen.get(p.getName()).rotateProperty();
    }

    private Circuit createCircuit() {
        return createCircuit((PowerSupply) elements_screen.get("B").getCircuitElement());
    }

    public static class CircuitBuilder {
        private List<CircuitElement> elements;
        private List<Circuit.Connection> connections;
        private PowerSupply supply;

        public CircuitBuilder(List<CircuitElement> elements, List<Circuit.Connection> connections, PowerSupply supply) {
            this.elements = elements;
            this.connections = connections;
            this.supply = supply;
        }

        public Circuit circuit() {
            elements.remove(supply);
            simplifyCircuit();
            return new Circuit(supply, elements.get(0), elements);
        }

        public void simplifyCircuit() {
            while (elements.size() > 2) {
                for (int i = 0; i < connections.size(); i++) {
                    Circuit.Connection a = connections.get(i);
                    if(a.component1 == supply || a.component2 == supply) continue;

                    List<CircuitElement> in1 = connections.stream().filter((c)-> c.component2==a.component1).map((c)->c.component1).collect(Collectors.toList());
                    List<CircuitElement> out1 = connections.stream().filter((c)-> c.component1==a.component1).map((c)->c.component2).collect(Collectors.toList());
                    List<CircuitElement> in2 = connections.stream().filter((c)-> c.component2==a.component2).map((c)->c.component1).collect(Collectors.toList());
                    List<CircuitElement> out2 = connections.stream().filter((c)-> c.component1==a.component2).map((c)->c.component2).collect(Collectors.toList());
                    if (in1.size()==1 && out1.size()==1 && in2.size()==1 && out2.size()==1) {
                        connections.remove(i);
                        i--;
                        CircuitElement new_node = new SeriesResistorCapacitor((ResistorCapacitor) a.component1, (ResistorCapacitor) a.component2);
                        //connect previous nodes to new node
                        connections.stream().filter((c) -> c.component2 == a.component1).iterator().next().component2 = new_node;
                        connections.stream().filter((c) -> c.component1 == a.component2).iterator().next().component1 = new_node;
                        elements.remove(a.component1);
                        elements.remove(a.component2);
                        elements.add(new_node);
                    }
                }
                for (int i = 0; i < elements.size(); i++) {
                    CircuitElement a = elements.get(i);
                    for (int j = i + 1; j < elements.size(); j++) {
                        CircuitElement b = elements.get(j);
                        List<CircuitElement> ain = connections.stream().filter((c)-> c.component2==a).map((c)->c.component1).collect(Collectors.toList());
                        List<CircuitElement> aout = connections.stream().filter((c)-> c.component1==a).map((c)->c.component2).collect(Collectors.toList());
                        List<CircuitElement> bin = connections.stream().filter((c)-> c.component2==b).map((c)->c.component1).collect(Collectors.toList());
                        List<CircuitElement> bout = connections.stream().filter((c)-> c.component1==b).map((c)->c.component2).collect(Collectors.toList());
                        if (ain.equals(bin) && aout.equals(bout)) {
                            if (a instanceof Node || b instanceof Node) {
                                connections.removeIf((c) -> c.component1 == a || c.component2 == a);
                                elements.remove(a);
                            } else {
                                CircuitElement new_node = new ParallelResistorCapacitor((ResistorCapacitor) a, (ResistorCapacitor) b);
                                connections.removeIf((c) -> c.component1 == a || c.component2 == a);
                                var i1 = connections.stream().filter((c) -> c.component1 == b).iterator();
                                if(i1.hasNext()) i1.next().component1 = new_node;
                                var i2 = connections.stream().filter((c) -> c.component2 == b).iterator();
                                if(i2.hasNext()) i2.next().component2 = new_node;
                                elements.remove(a);
                                elements.remove(b);
                                elements.add(new_node);
                            }
                        }
                    }
                }
            }
        }
    }

    private Circuit createCircuit(PowerSupply e) {
        CircuitBuilder b = new CircuitBuilder(elements_screen.values().stream().map(ElementGUI::getCircuitElement)
                .collect(Collectors.toList()),
                arrow_screen.values().stream()
                        .map((a) -> new Circuit.Connection(a.getC1().getCircuitElement(), a.getC2().getCircuitElement()))
                        .collect(Collectors.toList()), e);
        return b.circuit();
    }
}
