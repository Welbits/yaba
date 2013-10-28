package com.pilasvacias.yaba.screens.lines;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.adapter.WBaseAdapter;
import com.pilasvacias.yaba.modules.emt.pojos.Line;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by IzanRodrigo on 16/10/13.
 */
public class LinesAdapter extends WBaseAdapter<Line, LinesAdapter.ViewHolder> {

    public LinesAdapter(Context context, int layoutResource) {
        super(context, layoutResource);
    }

    @Override
    protected void changeView(Line item, ViewHolder viewHolder) {
        viewHolder.label.setText(item.getLabel().trim());
        viewHolder.startEnd.setText(item.getNameA().trim() + " - " + item.getNameB().trim());
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    public void sort() {
        sort(Line.getLabelComparator());
    }

    static class ViewHolder {
        @InjectView(R.id.list_item_line_textViewLabel)
        TextView label;
        @InjectView(R.id.list_item_line_textViewStartEnd)
        TextView startEnd;

        public ViewHolder(View view) {
            Views.inject(this, view);
        }
    }

}
