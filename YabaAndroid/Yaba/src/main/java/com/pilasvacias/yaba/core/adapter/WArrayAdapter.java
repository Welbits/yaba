package com.pilasvacias.yaba.core.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import butterknife.Views;

import static com.pilasvacias.yaba.core.adapter.WArrayAdapter.ViewHolder;

/**
 * Created by IzanRodrigo on 16/10/13.
 */
public abstract class WArrayAdapter<T, U extends ViewHolder> extends ArrayAdapter<T> {

    private int layoutResource;

    public WArrayAdapter(Context context, int layoutResource) {
        super(context, layoutResource);
        this.layoutResource = layoutResource;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T item = getItem(position);
        U viewHolder;
        if (convertView != null) {
            viewHolder = (U) convertView.getTag();
        } else {
            convertView = View.inflate(getContext(), layoutResource, null);
            viewHolder = createViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        changeView(item, viewHolder);
        return convertView;
    }

    protected abstract void changeView(T item, U viewHolder);

    protected abstract U createViewHolder(View view);

    public static abstract class ViewHolder {
        public ViewHolder(View view) {
            Views.inject(this, view);
        }
    }
}
