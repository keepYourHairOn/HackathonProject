package com.directory.Model;

import java.awt.*;
import java.util.List;

public class LogicalElement {
    private final Wire outputWire;
    private final List<Wire> inputWires;
    private Point position;
    private final String name;
//    public final Guid
    public LogicalElement(Wire outputWire, List<Wire> inputWires, String name) {
        this.outputWire = outputWire;
        this.inputWires = inputWires;
        this.name = name;
    }


}
