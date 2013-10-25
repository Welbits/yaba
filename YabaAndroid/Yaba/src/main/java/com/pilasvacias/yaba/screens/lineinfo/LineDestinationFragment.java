package com.pilasvacias.yaba.screens.lineinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.network.NetworkFragment;
import com.pilasvacias.yaba.core.widget.EmptyView;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by Fede on 24/10/13.
 */
public class LineDestinationFragment extends NetworkFragment {
    @InjectView(R.id.simple_list_listView)
    ListView listView;

    private BusStopAdapter adapter;
    private String destination;

    public LineDestinationFragment(String name) {
        destination = name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_simple_list, container, false);
        Views.inject(this, rootView);

        adapter = new BusStopAdapter(getBaseActivity(), R.layout.list_item_bus_stop);

        EmptyView.makeText(R.string.empty_list).into(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//TODO
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO
                return true;
            }
        });
        listView.setAdapter(adapter);

        return rootView;
    }


}
