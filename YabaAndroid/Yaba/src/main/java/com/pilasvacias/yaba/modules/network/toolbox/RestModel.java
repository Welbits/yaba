package com.pilasvacias.yaba.modules.network.toolbox;

import com.google.gson.annotations.SerializedName;


public class RestModel<T> {

    @SerializedName("_payload")
    protected T payload;

    public T getPayLoad() {
        return payload;
    }
}
