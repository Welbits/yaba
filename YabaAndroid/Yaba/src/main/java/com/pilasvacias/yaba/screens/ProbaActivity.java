package com.pilasvacias.yaba.screens;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.experimental.MagicTurn;
import com.pilasvacias.yaba.core.experimental.Save;
import com.pilasvacias.yaba.core.experimental.Token;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.modules.emt.handlers.EmtErrorHandler;
import com.pilasvacias.yaba.modules.emt.handlers.EmtSuccessHandler;
import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.util.L;
import com.pilasvacias.yaba.util.Time;

import java.util.List;

import butterknife.InjectView;
import butterknife.Views;

public class ProbaActivity extends NetworkActivity {

    @InjectView(R.id.listView) ListView listView;
    @Save Line line;

    @Save(token = "lineListToken") List<Line> lineTest;
    @Token TypeToken<List<Line>> lineListToken = new TypeToken<List<Line>>() {};

    @Save(token = "testToken") EmtData<Line> test;
    @Token TypeToken<EmtData<Line>> testToken = new TypeToken<EmtData<Line>>() {};

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
        requestManager
                .beginChainedRequest()
                    .newLink(Line.class)
                        .body(new GetListLines())
                        .success(new EmtSuccessHandler<Line>() {
                            @Override public void onSuccess(final EmtData<Line> result) {
                                L.og.d("result 1");
                            }
                        })
                .fakeTime(3000)
                .endLink()
                    .newLink(Line.class)
                        .body(new GetListLines())
                        .success(new EmtSuccessHandler<Line>() {
                            @Override public void onSuccess(final EmtData<Line> result) {
                                L.og.d("result 2");
                            }
                        })
                    .fakeTime(3000)
                .endLink()
                    .newLink(Line.class)
                        .body(new GetListLines())
                        .success(new EmtSuccessHandler<Line>() {
                            @Override public void onSuccess(final EmtData<Line> result) {
                                L.og.d("result 3");
                            }
                        })
                    .fakeTime(3000)
                .endLink()
                .error(new EmtErrorHandler() {
                    @Override public void handleError(VolleyError volleyError) {
                        super.handleError(volleyError);
                    }
                })
                .execute();

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
