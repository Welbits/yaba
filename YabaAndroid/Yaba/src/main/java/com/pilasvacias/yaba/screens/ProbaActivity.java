package com.pilasvacias.yaba.screens;

import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.VolleyLog;
import com.google.gson.reflect.TypeToken;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.experimental.MagicTurn;
import com.pilasvacias.yaba.core.experimental.Save;
import com.pilasvacias.yaba.core.experimental.Token;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.modules.emt.models.EmtData;

import java.util.List;

import butterknife.InjectView;
import butterknife.Views;

public class ProbaActivity extends NetworkActivity {

    @InjectView(R.id.listView) ListView listView;
    @Save Line line;

    @Save(token = "lineListToken") List<Line> lineTest;
    @Token TypeToken<List<Line>> lineListToken = new TypeToken<List<Line>>() {
    };

    @Save(token = "testToken") EmtData<Line> test;
    @Token TypeToken<EmtData<Line>> testToken = new TypeToken<EmtData<Line>>() {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proba);
        MagicTurn.restore(this, savedInstanceState);
        Views.inject(this);

        VolleyLog.DEBUG = true;
    }

    @Override protected void onResume() {
        super.onResume();
        createRequest();
    }

    private void createRequest() {
    }

    public static class GetListLines extends EmtBody {
        String SelectDate = "19-8-2013";
        String Lines = " ";
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MagicTurn.save(this, outState);
    }

    public static class Line {
        String GroupNumber;
        String DateFirst;
        String DateEnd;
        String Line;
        String Label;
        String NameA;
        String NameB;
    }

}
