package com.pilasvacias.yaba.screens.lineinfo;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.adapter.WBaseAdapter;
import com.pilasvacias.yaba.modules.emt.pojos.BusStop;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by creania on 24/10/13.
 */
public class BusStopAdapter extends WBaseAdapter<BusStop, BusStopAdapter.ViewHolder> {

    public BusStopAdapter(Context context, int layoutResource) {
        super(context, layoutResource);
    }

    @Override
    protected void changeView(BusStop item, ViewHolder viewHolder) {
        viewHolder.name.setText(item.Name);
        viewHolder.number.setText(item.Node);
        // TODO lines and street

    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    static class ViewHolder {
        @InjectView(R.id.busStop_lisItem_lines)
        TextView lines;
        @InjectView(R.id.busStop_lisItem_number)
        TextView number;
        @InjectView(R.id.busStop_lisItem_name)
        TextView name;
        @InjectView(R.id.busStop_lisItem_street)
        TextView street;

        public ViewHolder(View view) {
            Views.inject(this, view);
        }
    }
}
