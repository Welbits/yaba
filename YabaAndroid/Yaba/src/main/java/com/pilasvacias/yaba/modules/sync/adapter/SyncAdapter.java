/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pilasvacias.yaba.modules.sync.adapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.table.TableUtils;
import com.pilasvacias.yaba.modules.emt.builders.EmtRequestManager;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.emt.persistence.EmtDBHelper;
import com.pilasvacias.yaba.modules.emt.pojos.Line;
import com.pilasvacias.yaba.modules.emt.pojos.LineStop;
import com.pilasvacias.yaba.modules.emt.pojos.Stop;
import com.pilasvacias.yaba.modules.emt.requests.GetListLines;
import com.pilasvacias.yaba.modules.emt.requests.GetNodesLines;
import com.pilasvacias.yaba.util.L;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Define a sync adapter for the app.
 * <p/>
 * <p>This class is instantiated in {@link SyncService}, which also binds SyncAdapter to the system.
 * SyncAdapter should only be initialized in SyncService, never anywhere else.
 * <p/>
 * <p>The system calls onPerformSync() via an RPC call through the IBinder object supplied by
 * SyncService.
 */
class SyncAdapter extends AbstractThreadedSyncAdapter {
    protected EmtRequestManager requestManager;
    protected RequestQueue requestQueue;

    /**
     * Project used when querying content provider. Returns all known fields.
     */

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        init(context);
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        init(context);
    }

    private void init(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        requestManager = new EmtRequestManager(requestQueue);
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the ormlite db is done here.
     * Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     * .
     * <p/>
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link android.content.AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     * <p/>
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        updateEmtDB();
    }

    private void updateEmtDB() {
        L.og.d("Updating EMT database");
        final EmtDBHelper dbHelper = OpenHelperManager.getHelper(getContext(), EmtDBHelper.class);
        L.time.begin("Updating EMT database");
        final EmtData<Stop> stops = getNodes();
        L.time.addMark("got %d stops from EMT", stops.getPayload().size());
        final EmtData<Line> lines = getLines();
        L.time.addMark("got %d lines from EMT", lines.getPayload().size());

        if (stops == null || lines == null) {
            L.time.addMark("could not get data from emt. stops = %s, lines %s", stops, lines);
            L.time.error();
            return;
        }

        final List<LineStop> lineStops = getLinesFromStops(stops.getPayload());
        L.time.addMark("finished creating %d relations of lines and stops", lineStops.size());

        //Save the relations
        dbHelper.getLinesStopsDao().callBatchTasks(new Callable<LineStop>() {
            @Override public LineStop call() throws Exception {
                L.time.addMark("clear old line stops relations");
                TableUtils.clearTable(dbHelper.getConnectionSource(), LineStop.class);
                L.time.addMark("start writing relations");
                for (LineStop lineStop : lineStops) {
                    dbHelper.getLinesStopsDao().create(lineStop);
                }
                L.time.addMark("finished writing line stops relations");
                lineStops.clear();

                return null;
            }
        });

        //Save the stops
        dbHelper.getStopsDao().callBatchTasks(new Callable<Object>() {
            @Override public Object call() throws Exception {
                L.time.addMark("clear old stops");
                TableUtils.clearTable(dbHelper.getConnectionSource(), Stop.class);
                L.time.addMark("start writing stops");
                for (Stop stop : stops.getPayload()) {
                    dbHelper.getStopsDao().create(stop);
                }
                L.time.addMark("finished writing stops");
                stops.getPayload().clear();
                return null;
            }
        });

        //Save the lines
        dbHelper.getLinesDao().callBatchTasks(new Callable<Object>() {
            @Override public Object call() throws Exception {
                L.time.addMark("clear old lines");
                TableUtils.clearTable(dbHelper.getConnectionSource(), Line.class);
                L.time.addMark("start writing lines");
                for (Line line : lines.getPayload()) {
                    dbHelper.getLinesDao().create(line);
                }
                L.time.addMark("finished writing lines");
                lines.getPayload().clear();
                return null;
            }
        });
        L.time.end();
        OpenHelperManager.releaseHelper();
    }

    private List<LineStop> getLinesFromStops(List<Stop> stops) {
        //Stops format: 29/1 30/1 31/2
        LinkedList<LineStop> data = new LinkedList<>();
        for (Stop stop : stops) {
            String[] lines = stop.getLines().split("\\s+");
            for (String line : lines) {
                // 29/1 or 145/2
                String[] lineAndDirection = line.split("/");
                int lineNumber = Integer.parseInt(lineAndDirection[0]);
                int lineDirection = Integer.parseInt(lineAndDirection[1]);

                //Create the relation entry
                LineStop lineStop = new LineStop();
                Line relationLine = new Line();
                relationLine.setLineNumber(lineNumber);

                lineStop.setLine(relationLine);
                lineStop.setDirection(lineDirection);
                lineStop.setStop(stop);

                data.add(lineStop);
            }
        }

        return data;
    }

    private EmtData<Stop> getNodes() {
        GetNodesLines body = new GetNodesLines();
        body.setNodes(new String[]{}, true);

        return requestManager.beginRequest(Stop.class)
                .body(body)
                .cacheSkip(true)
                .cacheResult(false)
                .retryPolicy(new DefaultRetryPolicy(5000, 5, 2))
                .executeSync();

    }

    private EmtData<Line> getLines() {
        return requestManager.beginRequest(Line.class)
                .body(new GetListLines())
                .cacheSkip(true)
                .retryPolicy(new DefaultRetryPolicy(5000, 3, 2))
                .cacheResult(false)
                .executeSync();

    }


}
