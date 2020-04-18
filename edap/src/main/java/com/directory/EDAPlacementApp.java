package com.directory;

import com.directory.Model.*;
import javafx.util.Pair;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication(scanBasePackages = "com.directory.Model.controller")
@EnableWebMvc
public class EDAPlacementApp {
    public static MyGraph<String> graph;

    public static void main(String[] args) throws Exception {
        graph = Parser.parseFromFile(Paths.get(Paths.get("").toAbsolutePath() + "/edap/input.txt"));
        SpringApplication.run(EDAPlacementApp.class, args);
    }

    public static int[][][] circuit(){
        return BuildCircuit(graph);
    }

    public static int[][][] BuildCircuit(MyGraph<String> graph) {
        Circuit circuit = new Circuit();
        List<String> vertices = graph.AllVertices();
        ArrayList<Circuit.Element> allElements = new ArrayList<Circuit.Element>();
        LinkedList<Circuit.Element> inputs = new LinkedList<Circuit.Element>();
        LinkedList<Circuit.Element> outputs = new LinkedList<Circuit.Element>();
        LinkedList<Circuit.Element> elements = new LinkedList<Circuit.Element>();
        LinkedList<Pair<Circuit.Element,Circuit.Element>> wires = new LinkedList<Pair<Circuit.Element,Circuit.Element>>();

        for (String vertex:vertices ) {
            Circuit.Element element = circuit.createNew(vertex);
            allElements.add(element);
            HashSet<String> Inputs = graph.getAllInputs(vertex);
            HashSet<String> Outputs = graph.getAllOutputs(vertex);

            if(Inputs.isEmpty())
            {
                element.elementType = Circuit.PointType.Input;
                inputs.add(element);
            }
            else if(Outputs.isEmpty())
            {
                element.elementType = Circuit.PointType.Output;
                outputs.add(element);
            }
            else
            {
                element.elementType = ParseElement(vertex);
                elements.add(element);
            }
        }
        for (String vertex:vertices ) {
            HashSet<String> outs = graph.getAllOutputs(vertex);
            Circuit.Element element = FindeElement(allElements,vertex);
            for (String output : outs) {
                Circuit.Element element2 = FindeElement(allElements,output);
                wires.add(new Pair<>(element,element2));
            }
        }

        circuit.Build(elements,inputs,outputs,wires);
        int[][][] result = circuit.ToArray();

        return result;
    }

    private static Circuit.PointType ParseElement(String element)
    {
        String type = element.substring(0,element.indexOf('|'));
        switch(type)
        {
            case "and": return  Circuit.PointType.And;
            case "or": return  Circuit.PointType.Or;
            case "not": return  Circuit.PointType.Not;
        }
        return  Circuit.PointType.None;
    }

    private static Circuit.Element FindeElement(  ArrayList<Circuit.Element> allElements, String str)
    {
        for(int i = 0; i < allElements.size();i++)
        {
            if(allElements.get(i).name.equals(str))return allElements.get(i);
        }
        return  null;
    }
}
