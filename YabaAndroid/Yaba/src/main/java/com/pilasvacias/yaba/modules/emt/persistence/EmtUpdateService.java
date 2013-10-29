package com.pilasvacias.yaba.modules.emt.persistence;

import android.app.IntentService;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.pilasvacias.yaba.application.YabaApplication;
import com.pilasvacias.yaba.modules.emt.builders.EmtRequestManager;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.emt.pojos.Line;
import com.pilasvacias.yaba.modules.emt.pojos.Stop;
import com.pilasvacias.yaba.modules.emt.requests.GetListLines;
import com.pilasvacias.yaba.modules.emt.requests.GetNodesLines;
import com.pilasvacias.yaba.util.L;

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
        EmtData<Stop> nodes = getNodes();
        EmtData<Line> lines = getLines();
        if (nodes != null) {
            L.og.d("got %d nodes", nodes.getPayload().size());
        }
        if (lines != null) {
            L.og.d("got %d lines", lines.getPayload().size());
        }

        dbHelper.getRuntimeExceptionDao(Line.class).callBatchTasks(new Callable<Object>() {
            @Override public Object call() throws Exception {
                return null;
            }
        });
    }

    private EmtData<Stop> getNodes() {
        GetNodesLines body = new GetNodesLines();
        body.setNodes(new String[]{}, true);
        return requestManager.beginRequest(Stop.class)
                .body(body)
                .ignoreLoading(true)
                .cacheSkip(true)
                .cacheResult(false)
                .executeSync();
    }

    private EmtData<Line> getLines() {
        GetListLines body = new GetListLines();
        return requestManager.beginRequest(Line.class)
                .body(body)
                .ignoreLoading(true)
                .cacheSkip(true)
                .cacheResult(false)
                .executeSync();
    }

}
