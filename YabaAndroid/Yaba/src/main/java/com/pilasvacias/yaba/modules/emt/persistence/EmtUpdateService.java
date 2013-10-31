package com.pilasvacias.yaba.modules.emt.persistence;

import android.app.IntentService;
import android.content.Intent;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.table.TableUtils;
import com.pilasvacias.yaba.application.YabaApplication;
import com.pilasvacias.yaba.modules.emt.builders.EmtRequestManager;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.emt.pojos.Line;
import com.pilasvacias.yaba.modules.emt.pojos.LineStop;
import com.pilasvacias.yaba.modules.emt.pojos.Stop;
import com.pilasvacias.yaba.modules.emt.requests.GetListLines;
import com.pilasvacias.yaba.modules.emt.requests.GetNodesLines;
import com.pilasvacias.yaba.util.L;

import java.util.LinkedList;
import java.util.List;
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
        L.og.d("Updating DB");
        final EmtDBHelper dbHelper = OpenHelperManager.getHelper(this, EmtDBHelper.class);
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
        LinkedList<LineStop> data = new LinkedList<LineStop>();
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

    private EmtData<Stop> getNodes(int retries) {
        GetNodesLines body = new GetNodesLines();
        body.setNodes(new String[]{}, true);

        return requestManager.beginRequest(Stop.class)
                .body(body)
                .cacheSkip(true)
                .cacheResult(false)
                .retryPolicy(new DefaultRetryPolicy(5000, 5, 2))
                .executeSync();

    }

    private EmtData<Line> getLines(int retries) {
        return requestManager.beginRequest(Line.class)
                .body(new GetListLines())
                .cacheSkip(true)
                .retryPolicy(new DefaultRetryPolicy(5000, 3, 2))
                .cacheResult(false)
                .executeSync();

    }

}
