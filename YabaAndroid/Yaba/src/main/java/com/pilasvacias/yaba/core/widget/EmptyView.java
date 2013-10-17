package com.pilasvacias.yaba.core.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Created by Izan Rodrigo on 3/10/13.
 */
public class EmptyView {
    public static TextView makeText(ListView listView, int textResource) {
        return makeText(listView, listView.getContext().getString(textResource));
    }

    public static TextView makeText(ListView listView, String text) {
        TextView textView = makeText(listView.getContext(), text);
        int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(matchParent, matchParent);
        ViewGroup parent = (ViewGroup) listView.getParent();
        textView.setLayoutParams(layoutParams);
        if (parent != null) {
            View emptyView = listView.getEmptyView();
            if (emptyView != null) {
                parent.removeView(listView.getEmptyView());
            }
            parent.addView(textView);
        }
        return textView;
    }

    public static TextView makeText(Context context, int textResource) {
        return makeText(context, context.getString(textResource));
    }

    public static TextView makeText(Context context, String text) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        textView.setId(android.R.id.empty);
        return textView;
    }

}
