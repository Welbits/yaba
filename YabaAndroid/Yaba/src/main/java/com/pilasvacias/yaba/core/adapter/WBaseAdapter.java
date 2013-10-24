package com.pilasvacias.yaba.core.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by IzanRodrigo on 16/10/13.
 */
public abstract class WBaseAdapter<Item, ViewHolder> extends BaseAdapter {

    private ArrayList<Item> items;
    private int layoutResource;
    private Context context;

    /**
     * Returns a simple BaseAdapter wrapper that uses
     * ViewHolder pattern to improve list performance.
     * @param context Context needed by adapter.
     * @param layoutResource List item layout resource.
     */
    public WBaseAdapter(Context context, int layoutResource) {
        this.context = context;
        this.layoutResource = layoutResource;
        this.items = new ArrayList<Item>();
    }

    @Override
    public synchronized int getCount() {
        return items.size();
    }

    @Override
    public synchronized Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public synchronized long getItemId(int position) {
        return 0;
    }

    /**
     * Add item at the end of the adapter.
     * @param item
     */
    public synchronized void add(Item item) {
        items.add(item);
        notifyDataSetChanged();
    }

    /**
     * Inserts item at location.
     * @param item
     * @param location
     */
    public synchronized void add(Item item, int location) {
        items.add(location, item);
        notifyDataSetChanged();
    }

    /**
     * Add collection at the end of the adapter.
     * @param items
     */
    public synchronized void addAll(Collection<? extends Item> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * Add array at the end of the adapter.
     * @param items
     */
    public synchronized void addAll(Item... items) {
        addAll(Arrays.asList(items));
    }

    /**
     * Insert collection at location.
     * @param items
     */
    public synchronized void addAll(int location, List<? extends Item> items) {
        this.items.addAll(location, items);
        notifyDataSetChanged();
    }

    /**
     * Insert array at location.
     * @param items
     */
    public synchronized void addAll(int location, Item... items) {
        addAll(location, Arrays.asList(items));
    }

    /**
     * Remove item from the adapter.
     * @param item
     */
    public synchronized void remove(Item item) {
        items.remove(item);
        notifyDataSetChanged();
    }

    /**
     * Remove item at location.
     * @param location
     */
    public synchronized void remove(int location) {
        items.remove(location);
        notifyDataSetChanged();
    }

    /**
     * Remove all items.
     */
    public synchronized void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    /**
     * Sort items using Comparator.
     * @param comparator
     */
    public synchronized void sort(Comparator<Item> comparator) {
        Collections.sort(items, comparator);
        notifyDataSetChanged();
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, layoutResource, null);
            viewHolder = createViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        changeView(item, viewHolder);
        return convertView;
    }

    /**
     * Modify ViewHolder views as needed, using item.
     * @param item
     * @param viewHolder
     */
    protected abstract void changeView(Item item, ViewHolder viewHolder);

    /**
     * Method needed due to generic types limitation.
     * Simply return new ViewHolder(view).
     * @param view
     * @return
     */
    protected abstract ViewHolder createViewHolder(View view);
}
