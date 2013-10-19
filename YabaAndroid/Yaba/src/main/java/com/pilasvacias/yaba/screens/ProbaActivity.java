package com.pilasvacias.yaba.screens;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.experimental.InstanceSaver;
import com.pilasvacias.yaba.core.experimental.SaveCollectionState;
import com.pilasvacias.yaba.core.experimental.SaveObjectState;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.modules.emt.handlers.EmtSuccessHandler;
import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.util.L;

import java.util.List;

import butterknife.InjectView;
import butterknife.Views;

public class ProbaActivity extends NetworkActivity {

    @InjectView(R.id.listView)
    ListView listView;
    @SaveObjectState
    Line line;
    @SaveCollectionState(type = Line.class)
    List<Line> lineTest;
    @SaveObjectState
    EmtData<Line> test;

    @SaveObjectState
    boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proba);
        InstanceSaver.restore(this, savedInstanceState);
        Views.inject(this);
        if (firstTime) {
            createRequest();
            firstTime = false;
        } else {
            createAdapter();
        }
    }

    private void createAdapter() {
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
        if (lineTest != null)
            adapter.addAll(lineTest);
        adapter.add(line);
        adapter.add(line);
        adapter.add(line);
        adapter.add(line);
        adapter.add(line);
        adapter.add(line);
        listView.setAdapter(adapter);
    }

    private void createRequest() {
        requestManager
                .beginRequest(Line.class)
                .body(new GetListLines())
                .success(new EmtSuccessHandler<Line>() {
                    @Override public void onSuccess(final EmtData<Line> result) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        L.og.d("result =>\n %s", gson.toJson(result));
                        lineTest = result.getPayload();
                        line = result.getPayload().get(0);
                        test = result;
                        createAdapter();
                    }
                })
                .cacheSkip(true)
                .execute();
    }

    public static class GetListLines extends EmtBody {
        String SelectDate = "19-8-2013";
        String Lines = "143|90|1";
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        InstanceSaver.save(this, outState);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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

    @Override protected void onDestroy() {
        super.onDestroy();
    }
}
