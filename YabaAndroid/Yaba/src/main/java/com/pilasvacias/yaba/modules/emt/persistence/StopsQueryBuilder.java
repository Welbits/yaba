package com.pilasvacias.yaba.modules.emt.persistence;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.pilasvacias.yaba.modules.emt.pojos.Stop;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Pablo Orgaz - 11/1/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class StopsQueryBuilder {


    private static final int AND = 0;
    private static final int OR = 1;
    private EmtDBHelper dbHelper;
    private QueryBuilder<Stop, Integer> query;
    private Where<Stop, Integer> where;
    private boolean firstWhere = true;

    public StopsQueryBuilder(EmtDBHelper dbHelper) {
        this.dbHelper = dbHelper;
        query = dbHelper.getStopsDao().queryBuilder();
        where = query.where();
    }

    public StopsQueryBuilder query(String queryText) {
        queryText = queryText.trim();
        String[] args = queryText.split("\\s+");
        if (args.length > 1) {
            //There are multiple words, like burgos 30
            //safe to assume that you are not entering a stop number
            for (String arg : args) {
                name(arg, AND);
            }
        } else {
            //Its only numbers, match for stop number
            if (queryText.matches("\\d+"))
                stop(Integer.parseInt(queryText), OR);
            else
                name(queryText, OR);
        }
        return this;
    }

    public StopsQueryBuilder name(String name, int mode) {
        if (name == null)
            return this;
        try {
            where = addWhere(mode).like("name", "%" + name + "%");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public StopsQueryBuilder stop(int number, int mode) {
        if (number == -1)
            return this;
        try {
            where = addWhere(mode).idEq(number);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public StopsQueryBuilder pos(double posX, double posY, double radius, int mode) {
        try {
            where = addWhere(mode).between("posy", posX - radius, posY + radius)
                    .and().between("posY", posY - radius, posY + radius);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public List<Stop> execute() {
        query.setWhere(where);
        try {
            return dbHelper.getStopsDao().query(query.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private Where<Stop, Integer> addWhere(int mode) {
        if (firstWhere) {
            firstWhere = false;
            return where;
        } else {
            if (mode == AND)
                return where.and();
            else if (mode == OR)
                return where.or();
            else return where.or();
        }
    }

}
