package com.pilasvacias.yaba.screens.nocturnos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyLog;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.network.NetworkFragment;
import com.pilasvacias.yaba.core.widget.EmptyView;
import com.pilasvacias.yaba.modules.emt.handlers.EmtSuccessHandler;
import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.screens.lines.Line;
import com.pilasvacias.yaba.screens.lines.LinesAdapter;
import com.pilasvacias.yaba.screens.lines.LinesRegex;
import com.pilasvacias.yaba.util.Date;
import com.pilasvacias.yaba.util.Time;
import com.pilasvacias.yaba.util.WToast;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by IzanRodrigo on 16/10/13.
 */
public class NocturnosFragment extends NetworkFragment {

    // Inject views
    @InjectView(R.id.simple_list_listView)
    ListView listView;
    // Fields
    private LinesAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        VolleyLog.DEBUG = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_simple_list, container, false);
        Views.inject(this, rootView);
        configureListView();
        return rootView;
    }

    private void configureListView() {
        adapter = new LinesAdapter(getBaseActivity(), R.layout.list_item_line);

        listView.setEmptyView(EmptyView.makeText(listView, R.string.empty_list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Line item = adapter.getItem(position);
                WToast.showShort(getBaseActivity(), item);
            }
        });
        listView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerBus();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadLines();
    }

    public void loadLines() {
        getRequestManager()
                .beginRequest(Line.class)
                .body(new GetListLines())
                .success(new EmtSuccessHandler<Line>() {
                    @Override
                    public void onSuccess(final EmtData<Line> result) {
                        for (Line line : result.getPayload()) {
                            if (line.Label.matches(LinesRegex.NIGHTLY)) {
                                adapter.add(line);
                            }
                        }
                    }
                })
                .cacheTime(Time.days(1D))
                .execute();
    }

    public static class GetListLines extends EmtBody {
        String SelectDate = Date.getToday();
        String Lines = ""; //Todas las líneas
    }

}
