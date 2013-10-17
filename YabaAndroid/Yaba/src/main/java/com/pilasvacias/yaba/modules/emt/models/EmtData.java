package com.pilasvacias.yaba.modules.emt.models;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by pablo on 10/16/13.
 */
public class EmtData<T> {
    EmtInfo emtInfo = new EmtInfo();
    List<T> payload = new LinkedList<T>();

    public List<T> getPayload() {
        return payload;
    }

    public void setPayload(List<T> payload) {
        this.payload = payload;
    }

    public EmtInfo getEmtInfo() {
        return emtInfo;
    }

    public void setEmtInfo(EmtInfo emtInfo) {
        this.emtInfo = emtInfo;
    }
}
