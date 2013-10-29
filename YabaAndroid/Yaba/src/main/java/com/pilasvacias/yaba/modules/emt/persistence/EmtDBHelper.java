package com.pilasvacias.yaba.modules.emt.persistence;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.pilasvacias.yaba.modules.emt.pojos.Line;
import com.pilasvacias.yaba.modules.emt.pojos.Stop;
import com.pilasvacias.yaba.util.L;

import java.sql.SQLException;

/**
 * Created by Pablo Orgaz - 10/27/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class EmtDBHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "emtdb.db";
    public static final int DB_VERSION = 3;
    private Dao<Stop, Integer> stopsDao;
    private Dao<Line, Integer> linesDao;

    public EmtDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        L.og.d("Creating database for EMT");
        try {
            TableUtils.createTableIfNotExists(connectionSource, Stop.class);
            TableUtils.createTableIfNotExists(connectionSource, Line.class);
        } catch (SQLException e) {
            L.og.e(e.getCause(), "Error creating database");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int i, int i2) {
        L.og.d("Upgrading database for EMT (actually dropping the database for now)");
        try {
            TableUtils.dropTable(connectionSource, Stop.class, true);
            TableUtils.dropTable(connectionSource, Line.class, true);
            TableUtils.createTableIfNotExists(connectionSource, Stop.class);
            TableUtils.createTableIfNotExists(connectionSource, Line.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the Line Dao or null is something went wrong
     */
    public Dao<Line, Integer> getLinesDao() {
        if (linesDao == null) {
            try {
                linesDao = getDao(Line.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return linesDao;
    }

    /**
     * @return the Stops Dao or null is something went wrong
     */
    public Dao<Stop, Integer> getStopsDao() {
        if (stopsDao == null) {
            try {
                stopsDao = getDao(Stop.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return stopsDao;
    }

    @Override public void close() {
        super.close();
        stopsDao = null;
        linesDao = null;
    }
}
