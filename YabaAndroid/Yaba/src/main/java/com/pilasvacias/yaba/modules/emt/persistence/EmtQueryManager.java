package com.pilasvacias.yaba.modules.emt.persistence;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by Pablo Orgaz - 11/1/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class EmtQueryManager {

    EmtDBHelper dbHelper;
    Context context;

    public void init(Context context) {
        this.context = context;
        dbHelper = OpenHelperManager.getHelper(context, EmtDBHelper.class);
    }

    public void release() {
        OpenHelperManager.releaseHelper();
    }

    public StopsQueryBuilder stops() {
        return new StopsQueryBuilder(dbHelper);
    }
}
