package com.pilasvacias.yaba.screens.lines;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyLog;
import com.google.gson.reflect.TypeToken;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.adapter.WBaseAdapter;
import com.pilasvacias.yaba.core.adapter.WBaseAdapterNative;
import com.pilasvacias.yaba.core.experimental.MagicTurn;
import com.pilasvacias.yaba.core.experimental.Save;
import com.pilasvacias.yaba.core.experimental.Token;
import com.pilasvacias.yaba.core.network.NetworkFragment;
import com.pilasvacias.yaba.core.widget.EmptyView;
import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.emt.pojos.Line;
import com.pilasvacias.yaba.modules.playa.handlers.SuccessHandler;
import com.pilasvacias.yaba.screens.lineinfo.LineInfoActivity;
import com.pilasvacias.yaba.util.DateUtils;
import com.pilasvacias.yaba.util.Time;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by IzanRodrigo on 16/10/13.
 */
public class LineListFragment extends NetworkFragment {

    // Constants
    private static final String LIST_TYPE_KEY = "list_type";
    // Inject views
    @InjectView(R.id.simple_list_listView)
    ListView listView;
    // Fields
    private WBaseAdapter<Line> adapter;
    private LineListType listType;
    @Save(token = "lineToken")
    private EmtData<Line> lineEmtData;
    @Token
    private TypeToken<EmtData<Line>> lineToken = new TypeToken<EmtData<Line>>() {
    };

    public static Fragment newInstance(LineListType type) {
        Fragment fragment = new LineListFragment();
        Bundle args = new Bundle();
        args.putInt(LIST_TYPE_KEY, type.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        VolleyLog.DEBUG = true;
        listType = LineListType.values()[getArguments().getInt(LIST_TYPE_KEY)];
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
        adapter = new WBaseAdapterNative<Line>(getBaseActivity(), R.layout.list_item_line) {
            @Override
            protected void changeView(Line line, Inflater inflater) {
                TextView label = inflater.inflate(R.id.list_item_line_textViewLabel);
                label.setText(line.getLabel());
                TextView startEnd = inflater.inflate(R.id.list_item_line_textViewStartEnd);
                startEnd.setText(line.getNameA() + " - " + line.getNameB());
            }
        };

        EmptyView.makeText(R.string.empty_list).into(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Line item = adapter.getItem(position);
                Intent intent = new Intent(getBaseActivity(), LineInfoActivity.class);
                intent.putExtra(LineInfoActivity.LINE_KEY, item.toString());
                startActivity(intent);
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
        MagicTurn.restore(this, savedInstanceState);
        if (lineEmtData == null) {
            loadLines();
        } else {
            addLines(lineEmtData);
        }
    }

    private void addLines(EmtData<Line> lineEmtData) {
        for (Line line : lineEmtData.getPayload()) {
            if (listType.check(line)) {
                adapter.add(line);
            }
        }
        adapter.sort(Line.getLabelComparator());
    }

    public void loadLines() {
        getRequestManager()
                .beginRequest(Line.class)
                .body(new GetListLines())
                .success(new SuccessHandler<EmtData<Line>>() {
                    @Override public void onSuccess(EmtData<Line> result) {
                        lineEmtData = result;
                        addLines(result);
                    }
                })
                .cacheKey("lines")
                .verbose(true)
                .cacheTime(Time.days(1D))
                .execute();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MagicTurn.save(this, outState);
    }

    public enum LineListType {
        DAILY(LinesRegex.DAILY), NIGHTLY(LinesRegex.NIGHTLY), UNIVERSITY(LinesRegex.UNIVERSITY);
        private final String regex;

        LineListType(String regex) {
            this.regex = regex;
        }

        public boolean check(Line line) {
            if (regex.equals(LinesRegex.DAILY)) {
                return !line.getLabel().matches(LinesRegex.NIGHTLY) && !line.getLabel().matches(LinesRegex.UNIVERSITY);
            }
            return line.getLabel().matches(regex);
        }
    }

    public static class GetListLines extends EmtBody {
        String SelectDate = DateUtils.getToday();
        String Lines = ""; //Todas las líneas
    }
}
