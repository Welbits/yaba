package com.pilasvacias.yaba.screens.favorites;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.adapter.WArrayAdapter;

import butterknife.InjectView;

/**
 * Created by IzanRodrigo on 16/10/13.
 */
public class FavoritesAdapter extends WArrayAdapter<String, FavoritesAdapter.ViewHolder> {

    public FavoritesAdapter(Context context, int layoutResource) {
        super(context, layoutResource);
    }

    @Override
    protected void changeView(String item, ViewHolder viewHolder) {
        viewHolder.textView.setText(item);
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    static class ViewHolder extends WArrayAdapter.ViewHolder {
        @InjectView(R.id.simple_list_item_textView)
        TextView textView;

        public ViewHolder(View view) {
            super(view);
        }
    }

}
