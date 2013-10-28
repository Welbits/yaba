package com.pilasvacias.yaba.modules.emt.persistence;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Created by Pablo Orgaz - 10/27/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class EmtDBHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "emtdb";
    public static final int DB_VERSION = 1;

    public EmtDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int i, int i2) {

    }
}
