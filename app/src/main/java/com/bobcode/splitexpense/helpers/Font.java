package com.bobcode.splitexpense.helpers;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by vijayananjalees on 4/11/15.
 */
public class Font {

    private static Font font;

    public Typeface atwriter;


    private Font() {
    }

    public static Font getInstance(Context context) {
        if (font == null) {
            font = new Font();
            font.init(context);
        }
        return font;
    }

    public void init(Context context) {
        atwriter = Typeface.createFromAsset(context.getAssets(), "fonts/atwriter.ttf");
    }
}
