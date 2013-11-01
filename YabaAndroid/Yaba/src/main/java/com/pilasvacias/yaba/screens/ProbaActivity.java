package com.pilasvacias.yaba.screens;

import android.os.Bundle;

import com.android.volley.VolleyLog;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.experimental.MagicTurn;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.modules.emt.persistence.EmtDBHelper;
import com.pilasvacias.yaba.modules.emt.pojos.LineStop;
import com.pilasvacias.yaba.modules.emt.pojos.Stop;
import com.pilasvacias.yaba.util.WToast;

import java.sql.SQLException;
import java.util.List;

public class ProbaActivity extends NetworkActivity {

    EmtDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proba);
        MagicTurn.restore(this, savedInstanceState);
        VolleyLog.DEBUG = true;
        dbHelper = OpenHelperManager.getHelper(this, EmtDBHelper.class);
        dbHelper.updateDB(this);

        QueryBuilder<LineStop, Integer> lineStopQueryBuilder
                = dbHelper.getLinesStopsDao().queryBuilder();

        QueryBuilder<Stop, Integer> stopQueryBuilder =
                dbHelper.getStopsDao().queryBuilder();

        try {
            lineStopQueryBuilder.where().eq(LineStop.LINE_COLUMN_NAME, 90);
            //lineStopQueryBuilder.selectColumns(LineStop.STOP_COLUMN_NAME);
            List<Stop> result = dbHelper.getStopsDao().query(stopQueryBuilder.join(lineStopQueryBuilder).prepare());
            WToast.showShort(this, result);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override protected void onResume() {
        super.onResume();
        createRequest();
    }

    private void createRequest() {
//        getRequestManager().beginRequest(Line.class)
//                .body(new GetListLines())
//                .success(new EmtSuccessHandler<Line>() {
//                    @Override public void onSuccess(EmtData<Line> result) {
//                        WToast.showShort(ProbaActivity.this, result.getPayload().get(0));
//                    }
//                })
//                .fakeTime(Time.seconds(10))
//                .execute();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MagicTurn.save(this, outState);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        OpenHelperManager.releaseHelper();
        dbHelper = null;
    }
}
