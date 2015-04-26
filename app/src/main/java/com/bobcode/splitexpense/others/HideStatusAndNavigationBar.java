package com.bobcode.splitexpense.others;

import android.app.Activity;
import android.os.Build;
import android.view.View;

/**
 * Created by vijayananjalees on 4/12/15.
 */
public final class HideStatusAndNavigationBar {
    Activity activity;

    public HideStatusAndNavigationBar(Activity activity) {
        this.activity = activity;
    }

    public void hideStatusAndNavigationBar() {
        if (Build.VERSION.SDK_INT >= 10) {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.

            //SYSTEM_UI_FLAG_IMMERSIVE flag for lets your app go truly "full screen.
            //This flag, when combined with the SYSTEM_UI_FLAG_HIDE_NAVIGATION and SYSTEM_UI_FLAG_FULLSCREEN flags,
            // hides the navigation and status bars and lets your app capture all touch events on the screen.
            //if you'd like the system bars to automatically hide again after a few moments,
            // you can instead use the SYSTEM_UI_FLAG_IMMERSIVE_STICKY flag.

            //immersive" flags only take effect if you use them in conjunction with
            // SYSTEM_UI_FLAG_HIDE_NAVIGATION, SYSTEM_UI_FLAG_FULLSCREEN, or both.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions =
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //hide navigation bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN    //hide status bar

                            // keep the content from resizing when the system bars hide and show
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
