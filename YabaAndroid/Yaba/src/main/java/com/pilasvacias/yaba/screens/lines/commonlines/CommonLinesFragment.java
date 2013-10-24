package com.pilasvacias.yaba.screens.lines.commonlines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
import com.pilasvacias.yaba.util.L;
import com.pilasvacias.yaba.util.Time;
import com.pilasvacias.yaba.util.WToast;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Izan Rodrigo on 24/10/13.
 */
public class CommonLinesFragment extends NetworkFragment {

    // Inject views
    @InjectView(R.id.simple_list_listView)
    ListView listView;
    // Fields
    private LinesAdapter adapter;

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.lines, menu);
    }

    public void loadLines() {
        getRequestManager()
                .beginRequest(Line.class)
                .body(new GetListLines())
                .success(new EmtSuccessHandler<Line>() {
                    @Override
                    public void onSuccess(final EmtData<Line> result) {
                        L.og.d("result => %s", 1);
                        for (Line line : result.getPayload()) {
                            if (!line.Label.matches(LinesRegex.NOCTURNOS) && !line.Label.matches(LinesRegex.UNIVERSITARIOS)) {
                                adapter.add(line);
                            }
                        }
                    }
                })
                .cacheSkip(true)
                .cacheTime(Time.days(1D))
                .execute();
    }

    public static class GetListLines extends EmtBody {
        String SelectDate = Date.getToday();
        String Lines = ""; //Todas las líneas
    }

}
