package com.pilasvacias.yaba.core.adapter;

import android.content.Context;
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
public abstract class WBaseAdapter<Item> extends BaseAdapter {

    private ArrayList<Item> items;
    protected int layoutResource;
    protected Context context;

    /**
     * Returns a simple BaseAdapter wrapper that uses
     * ViewHolder pattern to improve list performance.
     *
     * @param context        Context needed by adapter.
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
        return position;
    }

    /**
     * Add item at the end of the adapter.
     *
     * @param item
     */
    public synchronized void add(Item item) {
        items.add(item);
        notifyDataSetChanged();
    }

    /**
     * Inserts item at location.
     *
     * @param item
     * @param location
     */
    public synchronized void add(Item item, int location) {
        items.add(location, item);
        notifyDataSetChanged();
    }

    /**
     * Add collection at the end of the adapter.
     *
     * @param items
     */
    public synchronized void addAll(Collection<? extends Item> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * Add array at the end of the adapter.
     *
     * @param items
     */
    public synchronized void addAll(Item... items) {
        addAll(Arrays.asList(items));
    }

    /**
     * Insert collection at location.
     *
     * @param items
     */
    public synchronized void addAll(int location, List<? extends Item> items) {
        this.items.addAll(location, items);
        notifyDataSetChanged();
    }

    /**
     * Insert array at location.
     *
     * @param items
     */
    public synchronized void addAll(int location, Item... items) {
        addAll(location, Arrays.asList(items));
    }

    /**
     * Remove item from the adapter.
     *
     * @param item
     */
    public synchronized void remove(Item item) {
        items.remove(item);
        notifyDataSetChanged();
    }

    /**
     * Remove item at location.
     *
     * @param location
     */
    public synchronized void remove(int location) {
        items.remove(location);
        notifyDataSetChanged();
    }

    /**
     * Returns all items in the adapter, but not removes them.
     *
     * @return all items in the adapter
     */
    public ArrayList<Item> getAllItems() {
        return items;
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
     *
     * @param comparator
     */
    public synchronized void sort(Comparator<Item> comparator) {
        Collections.sort(items, comparator);
        notifyDataSetChanged();
    }
}
