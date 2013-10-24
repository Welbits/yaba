package com.pilasvacias.yaba.core.model;

import com.google.gson.Gson;

/**
 * Created by IzanRodrigo on 24/10/13.
 */
public class Model {
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
