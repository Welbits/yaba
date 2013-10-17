package com.pilasvacias.yaba.screens;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.modules.emt.handlers.EmtSuccessHandler;
import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.util.L;
import com.pilasvacias.yaba.modules.util.Time;


import butterknife.InjectView;
import butterknife.Views;

public class ProbaActivity extends NetworkActivity {

    @InjectView(R.id.listView)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proba);
        Views.inject(this);
        createRequest();
    }

    private void createRequest() {

        VolleyLog.DEBUG = true;

        requestManager
                .beginRequest(Line.class)
                .body(new GetListLines())
                .success(new EmtSuccessHandler<Line>() {
                    @Override public void onSuccess(final EmtData<Line> result) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        L.og.d("result =>\n %s", gson.toJson(result));

                        ArrayAdapter<Line> adapter =
                                new ArrayAdapter<Line>(ProbaActivity.this, android.R.layout.activity_list_item) {
                                    @Override
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        TextView t = new TextView(getContext());
                                        Line line = getItem(position);
                                        t.setText(line.Line + " " + line.NameA + " " + line.NameB);
                                        return t;
                                    }
                                };
                        adapter.addAll(result.getPayload());
                        listView.setAdapter(adapter);

                    }
                })
                .verbose(true)
                .cacheTime(Time.minutes(1.5))
                .execute();
    }

    public static class GetListLines extends EmtBody {
        String SelectDate = "19-8-2013";
        String Lines = "143|90|1";
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
