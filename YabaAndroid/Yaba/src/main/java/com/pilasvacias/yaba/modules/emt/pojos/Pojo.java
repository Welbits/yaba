package com.pilasvacias.yaba.modules.emt.pojos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Created by Pablo Orgaz - 10/26/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class Pojo {

    @XStreamOmitField
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override public String toString() {
        return gson.toJson(this);
    }
}
