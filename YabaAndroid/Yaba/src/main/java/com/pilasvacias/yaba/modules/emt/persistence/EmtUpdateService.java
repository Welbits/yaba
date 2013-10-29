package com.pilasvacias.yaba.modules.emt.persistence;

import android.app.IntentService;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.j256.ormlite.table.TableUtils;
import com.pilasvacias.yaba.application.YabaApplication;
import com.pilasvacias.yaba.modules.emt.builders.EmtRequestManager;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.emt.pojos.Line;
import com.pilasvacias.yaba.modules.emt.pojos.Stop;
import com.pilasvacias.yaba.modules.emt.requests.GetListLines;
import com.pilasvacias.yaba.modules.emt.requests.GetNodesLines;
import com.pilasvacias.yaba.util.L;

import java.util.Random;
import java.util.concurrent.Callable;

import javax.inject.Inject;

/**
 * Created by Pablo Orgaz - 10/28/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class EmtUpdateService extends IntentService {

    public static final String SERVICE_NAME = "EmtUpdateService";
    public static final String ACTION_UPDATE = "ActionUpdateDB";
    @Inject protected EmtRequestManager requestManager;
    @Inject protected RequestQueue requestQueue;
    @Inject protected EmtDBHelper dbHelper;

    public EmtUpdateService() {
        super(SERVICE_NAME);

    }

    @Override protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(ACTION_UPDATE))
            updateDB();
    }

    @Override public void onCreate() {
        super.onCreate();
        YabaApplication application = (YabaApplication) getApplication();
        application.getApplicationGraph().inject(this);
    }

    private void updateDB() {
        L.time.begin("UPDATING NODES AND LINES");
        final EmtData<Stop> stops = getNodes(3);
        L.time.addMark("got %d stops from EMT", stops.getPayload().size());
        final EmtData<Line> lines = getLines(3);
        L.time.addMark("got %d lines from EMT", lines.getPayload().size());

        if (stops == null || lines == null) {
            L.time.addMark("could not get data from emt. stops = %s, lines %s", stops, lines);
            L.time.error();
            return;
        }

        dbHelper.getRuntimeExceptionDao(Line.class).callBatchTasks(new Callable<Object>() {
            @Override public Object call() throws Exception {
                L.time.addMark("clear old lines");
                TableUtils.clearTable(dbHelper.getConnectionSource(), Line.class);
                L.time.addMark("start writing lines");
                for (Line line : lines.getPayload()) {
                    dbHelper.getLinesDao().create(line);
                }
                L.time.addMark("finished writing lines");
                return null;
            }
        });
        dbHelper.getRuntimeExceptionDao(Stop.class).callBatchTasks(new Callable<Object>() {
            @Override public Object call() throws Exception {
                L.time.addMark("clear old stops");
                TableUtils.clearTable(dbHelper.getConnectionSource(), Stop.class);
                L.time.addMark("start writing stops");
                for (Stop stop : stops.getPayload()) {
                    dbHelper.getStopsDao().create(stop);
                }
                L.time.addMark("finished writing stops");
                return null;
            }
        });
        L.time.end();
    }

    private EmtData<Stop> getNodes(int retries) {
        GetNodesLines body = new GetNodesLines();
        body.setNodes(new String[]{}, true);
        EmtData<Stop> data;

        do {
            data = requestManager.beginRequest(Stop.class)
                    .body(body)
                    .ignoreLoading(true)
                    .cacheSkip(true)
                    .cacheResult(false)
                    .executeSync();
            retries--;

            if (data == null) {
                try {
                    L.time.addMark("get lines failed, sleeping and retrying");
                    Thread.sleep(200 * new Random().nextInt(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (data == null && retries > 0);

        return data;
    }

    private EmtData<Line> getLines(int retries) {
        GetListLines body = new GetListLines();
        EmtData<Line> data;
        do {
            data = requestManager.beginRequest(Line.class)
                    .body(body)
                    .ignoreLoading(true)
                    .cacheSkip(true)
                    .cacheResult(false)
                    .executeSync();
            retries--;
            if (data == null) {
                try {
                    L.time.addMark("get lines failed, sleeping and retrying");
                    Thread.sleep(200 * new Random().nextInt(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (data == null && retries > 0);
        return data;
    }

}
