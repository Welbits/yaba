package com.pilasvacias.yaba.screens.lines;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.adapter.WBaseAdapter;

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
        viewHolder.label.setText(item.Label.trim());
        viewHolder.groupNumber.setText("GroupNumber = " + item.GroupNumber.trim());
        viewHolder.line.setText("LineNumber = " + item.Line.trim());
        viewHolder.startEnd.setText(item.NameA.trim() + " - " + item.NameB.trim());
        viewHolder.date.setText(item.DateFirst.trim() + " - " + item.DateEnd.trim());
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    static class ViewHolder {
        @InjectView(R.id.list_item_line_textViewLine)
        TextView line;
        @InjectView(R.id.list_item_line_textViewGroupNumber)
        TextView groupNumber;
        @InjectView(R.id.list_item_line_textViewLabel)
        TextView label;
        @InjectView(R.id.list_item_line_textViewStartEnd)
        TextView startEnd;
        @InjectView(R.id.list_item_line_textViewDate)
        TextView date;

        public ViewHolder(View view) {
            Views.inject(this, view);
        }
    }

}
