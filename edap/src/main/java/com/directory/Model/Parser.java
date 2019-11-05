package com.directory.Model;

import java.nio.file.Path;
import java.util.*;

public class Parser {
    public static MyGraph<String> parseFromFile(Path path) throws Exception {
        Scanner sc = new Scanner(path);
        HashMap<String, String> vertInp = new HashMap<String, String>();
        List<String> vert = new ArrayList<String>();
        List<String> ops = new ArrayList<String>();
        String line;
        String el0;
        String result;
        while (sc.hasNextLine()) {
            line = sc.nextLine().toLowerCase();
            String[] el = line.split(" ");
            switch (el[0]) {
                case "in":
                case "out":
                    vert.add(el[1]);
                    vertInp.put(el[1], el[1]);
                    break;
                case "not":
                    el0 = el[0] + "|" + UUID.randomUUID().toString();
                    vert.add(el0);
                    vertInp.put(el[2], el0);
                    result = line.replace(el[0], el0);
                    ops.add(result);
                    break;
                default:
                    el0 = el[0] + "|" + UUID.randomUUID().toString();
                    vert.add(el0);
                    vertInp.put(el[3], el0);
                    result = line.replace(el[0], el0);
                    ops.add(result);
                    break;
            }
        }
        sc.close();

        MyGraph<String> graph = new MyGraph<String>(vert.toArray(new String[vert.size()]), true);
        for (String op : ops) {
            String[] el = op.split(" ");
            switch (el[0].split("\\|")[0]) {
                case "and":
                case "or":
                case "nand":
                case "nor":
                case "xor":
                case "xnor":
                    graph.insertVertex(el[0]);
                    graph.insertEdge(vertInp.get(el[1]), el[0], 0);
                    graph.insertEdge(vertInp.get(el[2]), el[0], 0);
                    break;
                case "not":
                    graph.insertVertex(el[0]);
                    graph.insertEdge(vertInp.get(el[1]), el[0], 0);
                    graph.insertEdge(el[0], el[2], 0);
                    break;
            }
        }
        return graph;
    }
}
