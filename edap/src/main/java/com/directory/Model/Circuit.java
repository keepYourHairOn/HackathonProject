package com.directory.Model;

import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Circuit {
    private LinkedList<Layout> field;
    private Layout Elements;
    private int width;
    private int length;

    public Circuit( ) {
        this.field = new LinkedList<Layout>();
    }

    public class Layout
    {
        public Layout(int x,int y)
        {
            points = new PointType[y][];
            for(int i = 0;i<y;i++)
            {
                PointType[] line = new PointType[x];
                for(int j = 0;j<x;j++)
                {
                    line[j] = PointType.None;
                }
                points[i] = line;

            }
        }
        public PointType points[][];

        public int[][] CreateMap()
        {
            int[][] copy = new int[points.length][points[0].length];
            for(int i = 0;i<points.length;i++) {
                for (int j = 0; j < points[0].length; j++)
                {
                    if(points[i][j] == PointType.None){
                        copy[i][j] = 0;
                    }
                    else
                    {
                        copy[i][j] = -1;
                    }
                }
            }
            return copy;
        }
    }

    public enum PointType
    {
        None,
        Wire,
        Input,
        Output,
        And,
        Or,
        Not
    }

    public int[][][] ToArray()
    {
        int [][][] result = new int[field.size()][width][length];
        for(int i = 0; i <   result.length;i++)
        {
            for(int j = 0; j < result[0].length;j++)
            {
                for(int k = 0; k < result[0][0].length;k++)
                {
                    result[i][j][k] = field.get(i).points[j][k].ordinal();
                }
            }
        }
        return  result;
    }

    public Element createNew(String name)
    {
        return new Element(name);
    }
    //mock for element
    public class Element {
        public Element(String name){
            this.name = name;
        }
        public String name;
        public PointType elementType;
        public int x;
        public int y;

        @Override
        public String toString() {
            return "Element{" +
                    "name='" + name + '\'' +
                    ", elementType=" + elementType +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public void Build(List<Element> elements, List<Element> inputs, List<Element> outputs, List<Pair<Element,Element>> wires)
    {
        Point size = CalculateSize( inputs.size(), elements.size() , outputs.size());
        Elements = new Layout(size.x * 8 - 3,size.y * 8 - 3);
        width = size.y * 8 - 3;
        length = size.x * 8 - 3;
        PlaceInputs(inputs);
        PlaceElements(elements);
        PlaceOutputs(outputs);
        field.add(CreateNewLayout());

        for (Pair<Element,Element>  wire: wires ) {
            int layout = 0;
            boolean placed = false;

            while(!placed && layout < field.size())
            {
                placed = TryPlaceWire(field.get(layout),wire.getKey(),wire.getValue());
                layout++;
            }
            if(!placed)
            {
                field.add(CreateNewLayout());
                TryPlaceWire(field.get(layout),wire.getKey(),wire.getValue());
            }
        }
    }

    private Layout CreateNewLayout()
    {
        Layout newLayout = new Layout(length, width);

        for(int i = 0;i<width;i++)
        {
            System.arraycopy(Elements.points[i],0,newLayout.points[i],0,length);
        }
        return newLayout;
    }

    private Point CalculateSize(int numberOfInputs, int numberOfElements, int numberOfOutputs)
    {
        Point p = new Point();
        p.x = Math.max(numberOfInputs,numberOfOutputs);
        p.y = (int)Math.ceil((double)numberOfElements / p.x) + 2;
        return p;
    }

    private void PlaceElements(List<Element> elements)
    {
        int yPos = 8;
        int xPos = 0;
        for (Element element: elements) {
            DrawElement( xPos, yPos, element.elementType);
            element.x = xPos;
            element.y = yPos;
            xPos += 8;
            if(xPos >= length)
            {
                yPos += 8;
            }
        }
    }

    private void PlaceInputs(List<Element> inputs)
    {
        int pos = 0;
        for (Element element: inputs) {
            DrawElement(pos,0, element.elementType);
            element.x = pos;
            element.y = 0;
            pos += 8;
        }
    }

    private void PlaceOutputs(List<Element> outputs)
    {
        int pos = 0;
        for (Element element: outputs) {
            DrawElement(pos,width - 5, element.elementType);
            element.x = pos;
            element.y = width - 5;
            pos += 8;
        }
    }

    private void DrawElement(int x, int y, PointType type)
    {
        for(int i = x; i < x+5;i++)
            for(int j = y; j < y+5;j++)
                Elements.points[j][i] = type;
    }

    private boolean TryPlaceWire(Layout layout, Element sourcElement, Element targetElement)
    {
        int[][] map = DrawMap(layout,sourcElement,targetElement);
        Point wireEnd = new Point();
        Point initialPoint = new Point();
        initialPoint.x = sourcElement.x + 2;
        initialPoint.y= sourcElement.y + 2;
        boolean result = DrawWay(wireEnd,map,initialPoint);
        if(!result)
            return false;
        DrawWire(layout,map,wireEnd);
        return true;
    }

    private int[][] DrawMap(Layout layout,Element sourceElement, Element targetElement)
    {
        int[][] map = layout.CreateMap();
        for(int i = targetElement.x; i < targetElement.x+5;i++) {
            for (int j = targetElement.y; j < targetElement.y + 5; j++)
                map[j][i] = -2;
        }

        for(int i = sourceElement.x; i < sourceElement.x+5;i++) {
            for (int j = sourceElement.y; j < sourceElement.y + 5; j++)
                map[j][i] = 0;
        }
        return map;
    }

    private boolean DrawWay(Point reachmentPoint, int[][] map, Point initialPoint)
    {
        Queue<Pair<Point,Integer>> points = new ArrayDeque<>();
        Point P = new Point();
        P.x = initialPoint.x;
        P.y = initialPoint.y;
        points.add(new Pair<Point,Integer>(P,1));

        while(!points.isEmpty())
        {
            Pair<Point,Integer> p = points.poll();
            Point point = p.getKey();
            boolean isAcceptable = true;
            for(int i = -1; i < 2 && isAcceptable; i++)
            {
                for(int j = -1; j < 2 && isAcceptable; j++)
                {
                    if(CheckPoint(point,i,j) && map[point.y+j][point.x+i] == -1){
                        map[point.y][point.x] = -3;
                        isAcceptable = false;
                    }
                }
            }
            if(!isAcceptable)continue;
            int counter = p.getValue();
            map[point.y][point.x] = counter;

            for(int i = -1; i < 2; i++)
            {
                for(int j = -1; j < 2; j++)
                {
                    if(CheckPoint(point,i,j) && (i+j)%2!=0)
                    {
                        if(map[point.y+j][point.x+i] == -2)
                        {
                            reachmentPoint.x = point.x;
                            reachmentPoint.y = point.y;
                            return true;
                        }
                        if(map[point.y+j][point.x+i] == 0) {
                            Point tmp = new Point(point.x + i, point.y + j);
                            points.add(new Pair<Point, Integer>(tmp, counter + 1));
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean CheckPoint(Point p, int iOfffset, int jOffset)
    {
        return (!(iOfffset ==0 && jOffset ==0)
                && p.x+iOfffset >=0 && p.x + iOfffset < length
                && p.y+jOffset >=0 && p.y + jOffset < width);
    }


    private void DrawWire(Layout layout,int[][] map, Point wireEnd)
    {
        /*for (int i = 0; i < map.length; i++) {

            int[] arr2 = map[i];
            for (int k = 0; k < arr2.length; k++) {
                System.out.print(arr2[k] + " ");
            }
            System.out.println();
        }
        System.out.println("=================");*/
        int counter = map[wireEnd.y][wireEnd.x];
        Point current = wireEnd;
        while(counter > 0)
        {
            layout.points[current.y][current.x] = PointType.Wire;
            boolean isFound = false;
            for(int ii = -1; ii < 2 && !isFound; ii++) {
                for (int jj = -1; jj < 2&& !isFound; jj++) {
                    if (CheckPoint(current,ii,jj) && map[current.y + jj][current.x + ii] == counter - 1&& (ii+jj)%2!=0
                    ) {
                        if(layout.points[current.y + jj][current.x + ii] != PointType.None)
                            return;
                        current.x +=  ii;
                        current.y += jj;
                        counter--;
                        isFound = true;
                    }
                }
            }

        }
    }
}