package com.pilasvacias.yaba.modules.emt.pojos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Pablo Orgaz - 10/31/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
@DatabaseTable
public class LineStop {

    @DatabaseField(generatedId = true)
    private int _id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Line line;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Stop stop;
    @DatabaseField
    private int direction;

    public LineStop() {
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }
}
