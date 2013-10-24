package com.pilasvacias.yaba.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by pablo on 24/07/13.
 * <p/>
 * Utility class load fonts
 */
public class FontUtils {

    public static final String NORMAL = "todaylight.otf";
    public static final String BOLD = "todaybold.otf";
    public static final String DETAIL = "angelina.ttf";

    private static HashMap<String, Typeface> fonts = new HashMap<String, Typeface>();

    public static Typeface getFont(Context context, String name) {

        //No font given
        if (name == null || context == null)
            return Typeface.DEFAULT;

        Typeface typeface = fonts.get(name);
        //Try to load font if it was not cached
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + name);
            } catch (Exception e) {
                Log.e("FontUtils", "error loading - " + name + " - using default");
            }

            if (typeface != null)
                fonts.put(name, typeface);
            else
                typeface = Typeface.DEFAULT;

        }
        return typeface;
    }
}
