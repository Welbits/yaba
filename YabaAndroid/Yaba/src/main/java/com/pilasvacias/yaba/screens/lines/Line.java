package com.pilasvacias.yaba.screens.lines;

import com.google.gson.Gson;

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
        return new Gson().toJson(this);
    }
}