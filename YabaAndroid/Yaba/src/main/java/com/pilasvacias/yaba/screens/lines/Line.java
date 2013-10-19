package com.pilasvacias.yaba.screens.lines;

public class Line {
    public String GroupNumber;
    public String DateFirst;
    public String DateEnd;
    public String Line;
    public String Label;
    public String NameA;
    public String NameB;

    @Override
    public String toString() {
        return Label + " " + NameA + " " + NameB;
    }
}