package com.directory.util;

public class Pair<X, Y> {
    public final X x;
    public final Y y;
    public Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X getKey(){
        return this.x;
    }

    public Y getValue(){
        return this.y;
    }
}
