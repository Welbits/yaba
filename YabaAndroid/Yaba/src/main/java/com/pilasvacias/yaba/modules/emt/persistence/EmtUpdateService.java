package com.pilasvacias.yaba.modules.emt.persistence;

import android.app.IntentService;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pilasvacias.yaba.modules.emt.builders.EmtRequestManager;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.emt.pojos.Line;
import com.pilasvacias.yaba.modules.emt.pojos.Node;
import com.pilasvacias.yaba.modules.emt.requests.GetListLines;
import com.pilasvacias.yaba.modules.emt.requests.GetNodesLines;
import com.pilasvacias.yaba.util.L;

/**
 * Created by Pablo Orgaz - 10/28/13 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class EmtUpdateService extends IntentService {

    public static final String SERVICE_NAME = "EmtUpdateService";
    public static final String ACTION_UPDATE = "ActionUpdateDB";
    protected EmtRequestManager requestManager;
    protected RequestQueue requestQueue;

    public EmtUpdateService() {
        super(SERVICE_NAME);

    }

    @Override protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(ACTION_UPDATE))
            updateDB();
    }


    @Override public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
        requestManager = new EmtRequestManager(requestQueue);
        requestManager.setContext(this);
    }

    private void updateDB() {
        EmtData<Node> nodes = getNodes();
        EmtData<Line> lines = getLines();
        L.og.d("got %d nodes", nodes.getPayload().size());
        L.og.d("got %d lines", lines.getPayload().size());
    }

    private EmtData<Node> getNodes() {
        GetNodesLines body = new GetNodesLines();
        body.setNodes(new String[]{}, true);
        return requestManager.beginRequest(Node.class)
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
