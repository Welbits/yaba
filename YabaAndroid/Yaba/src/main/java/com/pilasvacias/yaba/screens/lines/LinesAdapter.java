package com.pilasvacias.yaba.screens.lines;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.adapter.WArrayAdapter;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by IzanRodrigo on 16/10/13.
 */
public class LinesAdapter extends WArrayAdapter<Line, LinesAdapter.ViewHolder> {

    public LinesAdapter(Context context, int layoutResource) {
        super(context, layoutResource);
    }

    @Override
    protected void changeView(Line item, ViewHolder viewHolder) {
        viewHolder.textView.setText(item.toString());
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    static class ViewHolder {
        @InjectView(R.id.simple_list_item_textView)
        TextView textView;

        public ViewHolder(View view) {
            Views.inject(this, view);
        }
    }

}
