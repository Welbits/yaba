package com.pilasvacias.yaba.modules.emt.pojos;

import com.j256.ormlite.table.DatabaseTable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by Pablo Orgaz - 10/26/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
@DatabaseTable
public class Node extends Pojo {

    @XStreamAlias("Node") private String node;
    @XStreamAlias("PosxNode") private String posX;
    @XStreamAlias("PosyNode") private String posY;
    @XStreamAlias("Name") private String name;
    @XStreamAlias("Lines") private String lines;

    public Node() {
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getPosX() {
        return posX;
    }

    public void setPosX(String posX) {
        this.posX = posX;
    }

    public String getPosY() {
        return posY;
    }

    public void setPosY(String posY) {
        this.posY = posY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Relación de las líneas que, en algún momento del día o tipo de día tienen parada allí. Se compone
     * de grupos de valores separados por un slash (/). La primera parte es el número de línea EMT. La
     * segunda parte es el sentido de viaje (1.- Ida 2.- Vuelta)
     */
    public String getLines() {
        return lines;
    }

    public void setLines(String lines) {
        this.lines = lines;
    }


}
