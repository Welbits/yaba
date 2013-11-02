package com.pilasvacias.yaba.modules.emt.persistence;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.pilasvacias.yaba.modules.emt.pojos.Line;
import com.pilasvacias.yaba.modules.emt.pojos.LineStop;
import com.pilasvacias.yaba.modules.emt.pojos.Stop;
import com.pilasvacias.yaba.modules.sync.util.SyncUtils;
import com.pilasvacias.yaba.util.L;

import java.sql.SQLException;

/**
 * Created by Pablo Orgaz - 10/27/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class EmtDBHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "emtdb.db";
    public static final int DB_VERSION = 7;
    private RuntimeExceptionDao<Stop, Integer> stopsDao;
    private RuntimeExceptionDao<Line, Integer> linesDao;
    private RuntimeExceptionDao<LineStop, Integer> linesStopsDao;

    public EmtDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        L.og.d("Creating database for EMT");
        try {
            TableUtils.createTableIfNotExists(connectionSource, Stop.class);
            TableUtils.createTableIfNotExists(connectionSource, Line.class);
            TableUtils.createTableIfNotExists(connectionSource, LineStop.class);
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
            TableUtils.dropTable(connectionSource, LineStop.class, true);
            TableUtils.createTableIfNotExists(connectionSource, Stop.class);
            TableUtils.createTableIfNotExists(connectionSource, Line.class);
            TableUtils.createTableIfNotExists(connectionSource, LineStop.class);
            SyncUtils.triggerRefresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the Line Dao or null is something went wrong
     */
    public RuntimeExceptionDao<Line, Integer> getLinesDao() {
        if (linesDao == null) {
            linesDao = getRuntimeExceptionDao(Line.class);
        }
        return linesDao;
    }

    /**
     * @return the Stops Dao or null is something went wrong
     */
    public RuntimeExceptionDao<Stop, Integer> getStopsDao() {
        if (stopsDao == null) {
            stopsDao = getRuntimeExceptionDao(Stop.class);
        }
        return stopsDao;
    }

    /**
     * @return the LinesStops Dao or null is something went wrong
     */
    public RuntimeExceptionDao<LineStop, Integer> getLinesStopsDao() {
        if (linesStopsDao == null) {
            linesStopsDao = getRuntimeExceptionDao(LineStop.class);
        }
        return linesStopsDao;
    }

    @Override public void close() {
        super.close();
        stopsDao = null;
        linesDao = null;
    }
}
