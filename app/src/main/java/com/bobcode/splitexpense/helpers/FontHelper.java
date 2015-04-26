package com.bobcode.splitexpense.helpers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by vijayananjalees on 4/11/15.
 */
public class FontHelper {

    private static Font font;

    public static void applyFont(View parentView, Context context) {
        font = Font.getInstance(context);
        apply((ViewGroup) parentView);
    }

    private static void apply(ViewGroup parentView) {
        for (int i = 0; i < parentView.getChildCount(); i++) {
            View view = parentView.getChildAt(i);

            if (view instanceof EditText) {
                ((EditText) view).setTypeface(font.atwriter);
            }

            if (view instanceof TextView) {
                ((TextView) view).setTypeface(font.atwriter);
            } else if (view instanceof ViewGroup && ((ViewGroup) view).getChildCount() > 0) {
                apply((ViewGroup) view);
            }
        }
    }
}
