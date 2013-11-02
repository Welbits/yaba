package com.pilasvacias.yaba.core.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by IzanRodrigo on 16/10/13.
 */
public abstract class WBaseAdapterNative<Item> extends WBaseAdapter<Item> {

    /**
     * Returns a simple BaseAdapter wrapper that uses
     * ViewHolder pattern to improve list performance.
     *
     * @param context        Context needed by adapter.
     * @param layoutResource List item layout resource.
     */
    public WBaseAdapterNative(Context context, int layoutResource) {
        super(context, layoutResource);
    }

    public static class Inflater {
        private View rootView;

        private Inflater(View rootView) {
            this.rootView = rootView;
        }

        public <T extends View> T inflate(int viewResource) {
            return WBaseAdapterNative.inflate(rootView, viewResource);
        }
    }

    public static <T extends View> T inflate(View rootView, int viewResource) {
        SparseArray<View> viewHolder = (SparseArray<View>) rootView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            rootView.setTag(viewHolder);
        }
        View childView = viewHolder.get(viewResource);
        if (childView == null) {
            childView = rootView.findViewById(viewResource);
            viewHolder.put(viewResource, childView);
        }
        return (T) childView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        if (convertView == null) {
            convertView = View.inflate(context, layoutResource, null);
        }
        changeView(item, new Inflater(convertView));
        return convertView;
    }

    /**
     * Modify ViewHolder views as needed, using item and SparseViewHolder.
     *
     * @param item
     */
    protected abstract void changeView(Item item, Inflater inflater);

}
