package com.pilasvacias.yaba.core.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Views;

/**
 * Created by IzanRodrigo on 16/10/13.
 */
public abstract class WBaseAdapter<T, U> extends BaseAdapter {

    private ArrayList<T> items;
    private int layoutResource;
    private Context context;

    public WBaseAdapter(Context context, int layoutResource) {
        this.context = context;
        this.layoutResource = layoutResource;
        this.items = new ArrayList<T>();
    }

    @Override
    public synchronized int getCount() {
        return items.size();
    }

    @Override
    public synchronized T getItem(int position) {
        return items.get(position);
    }

    @Override
    public synchronized long getItemId(int position) {
        return 0;
    }

    public synchronized void add(T item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public synchronized void add(T item, int location) {
        items.add(location, item);
        notifyDataSetChanged();
    }

    public synchronized void addAll(List<? extends T> item) {
        items.addAll(item);
        notifyDataSetChanged();
    }

    public synchronized void addAll(List<? extends T> item, int location) {
        items.addAll(location, item);
        notifyDataSetChanged();
    }

    public synchronized void remove(T item) {
        items.remove(item);
        notifyDataSetChanged();
    }

    public synchronized void remove(int location) {
        items.remove(location);
        notifyDataSetChanged();
    }

    public synchronized void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public synchronized void sort(Comparator<T> comparator) {
        Collections.sort(items, comparator);
        notifyDataSetChanged();
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T item = getItem(position);
        U viewHolder;
        if (convertView != null) {
            viewHolder = (U) convertView.getTag();
        } else {
            convertView = View.inflate(context, layoutResource, null);
            viewHolder = createViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        changeView(item, viewHolder);
        return convertView;
    }

    protected abstract void changeView(T item, U viewHolder);

    protected abstract U createViewHolder(View view);
}
