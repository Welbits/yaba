package com.pilasvacias.yaba.modules.emt.pojos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The pojo representing a stop. Everything is indexed because we perform searches based
 * on all the attributes and speed is key while disk space is secondary.
 * <p/>
 * {@literal
 * <Node>41</Node>
 * <PosxNode>441510,8</PosxNode>
 * <PosyNode>4478299</PosyNode>
 * <Name>Pº CASTELLANA-PZA.DE LIMA</Name>
 * <Lines>14/2 27/2 40/2 126/2 147/2 150/2 522/2 524/2</Lines>
 * }
 */
@DatabaseTable
public class Stop extends Pojo {

    /**
     * The id of the stop
     */
    @XStreamAlias("Node")
    @DatabaseField(id = true, index = true)
    private int node;
    //
    /**
     * position X in geocoodinates
     */
    @XStreamAlias("PosxNode")
    @DatabaseField(index = true)
    private String posX;
    //
    /**
     * position Y in geocordinates
     */
    @XStreamAlias("PosyNode")
    @DatabaseField(index = true)
    private String posY;
    //
    /**
     * The name of the stop
     */
    @XStreamAlias("Name")
    @DatabaseField(index = true)
    private String name;
    //
    /**
     * A list of lines this stop is connected to with the format
     * <p/>
     * {@code 29/1 30/1 31/2}
     * <p/>
     * being the first number the id of the line and the second number
     * the direction.
     */
    @XStreamAlias("Lines")
    @DatabaseField(index = true)
    private String lines;

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
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
