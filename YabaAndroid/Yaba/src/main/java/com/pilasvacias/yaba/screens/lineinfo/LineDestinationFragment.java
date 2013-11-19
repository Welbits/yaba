package com.pilasvacias.yaba.screens.lineinfo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.adapter.WBaseAdapter;
import com.pilasvacias.yaba.core.adapter.WBaseAdapterNative;
import com.pilasvacias.yaba.core.experimental.MagicTurn;
import com.pilasvacias.yaba.core.experimental.Save;
import com.pilasvacias.yaba.core.network.NetworkFragment;
import com.pilasvacias.yaba.core.widget.EmptyView;
import com.pilasvacias.yaba.modules.emt.models.EmtBody;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.emt.persistence.EmtQueryManager;
import com.pilasvacias.yaba.modules.emt.pojos.Line;
import com.pilasvacias.yaba.modules.emt.pojos.Stop;
import com.pilasvacias.yaba.util.DateUtils;
import com.pilasvacias.yaba.util.WToast;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Fede on 24/10/13.
 */
public class LineDestinationFragment extends NetworkFragment {

    // Constants
    private static final String LINE_KEY = "line";
    private static final String DESTINATION_KEY = "destination";
    private static final String DIRECTION_KEY = "direction";
    // Inject views
    @InjectView(R.id.simple_list_listView)
    ListView listView;
    // Fields
    @Save private Line line;
    @Save private String destination;
    @Save private EmtData<Stop> stopEmtData;
    @Save private int direction;
    private WBaseAdapter<Stop> adapter;
    private EmtQueryManager queryManager;

    public static Fragment newInstance(Line line, String destination, int direction) {
        Fragment fragment = new LineDestinationFragment();
        Bundle args = new Bundle();
        args.putString(LINE_KEY, line.toString());
        args.putString(DESTINATION_KEY, destination);
        args.putInt(DIRECTION_KEY, direction);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        VolleyLog.DEBUG = true;
        line = new Gson().fromJson(getArguments().getString(LINE_KEY), Line.class);
        destination = getArguments().getString(DESTINATION_KEY);
        direction = getArguments().getInt(DIRECTION_KEY);
        queryManager = new EmtQueryManager();
        queryManager.init(getActivity());
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
        adapter = new WBaseAdapterNative<Stop>(getBaseActivity(), R.layout.list_item_bus_stop) {
            @Override
            protected void changeView(Stop stop, Inflater inflater) {
                TextView lines = inflater.inflate(R.id.busStop_lisItem_lines);
                lines.setText(stop.getLines());
                TextView number = inflater.inflate(R.id.busStop_lisItem_number);
                number.setText(String.valueOf(stop.getStopNumber()));
                TextView name = inflater.inflate(R.id.busStop_lisItem_name);
                name.setText(stop.getName());
                TextView street = inflater.inflate(R.id.busStop_lisItem_street);
                //TODO: Street
            }
        };

        EmptyView.makeText(R.string.empty_list).into(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WToast.showShort(getBaseActivity(), adapter.getItem(position));
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                WToast.showShort(getBaseActivity(), adapter.getItem(position));
                return true;
            }
        });
        listView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (stopEmtData == null) {
            loadStops();
        } else {
            addStops(stopEmtData);
        }
    }

    private void addStops(EmtData<Stop> stopEmtData) {
        for (Stop stop : stopEmtData.getPayload()) {
//            if (listType.check(stop)) {
            adapter.add(stop);
//            }
        }
    }

    //FIXME: Not working
    public void loadStops() {
        EmtData<Stop> result = new EmtData<Stop>();
        result.setPayload(queryManager.stops().inLine(line.getLineNumber(), direction).execute());
        addStops(result);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MagicTurn.save(this, outState);
    }

    public class GetLineStops extends EmtBody {
        String SelectDate = DateUtils.getToday();
        String Lines = String.valueOf(line.getLineNumber());
    }

    @Override public void onDestroy() {
        super.onDestroy();
        queryManager.release();
    }
}
