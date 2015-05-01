package com.bobcode.splitexpense.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.constants.Constants;
import com.bobcode.splitexpense.helpers.DateAndTimeHelper;
import com.bobcode.splitexpense.others.HideStatusAndNavigationBar;
import com.bobcode.splitexpense.utils.MySharedPrefs;
import com.melnykov.fab.FloatingActionButton;

public class SplashScreenActivity extends ActionBarActivity implements View.OnClickListener {

    private FloatingActionButton imageButton;

    private TextView txtViewCurrentDate;
    private TextView txtViewCurrentTime;

    private Handler handler = new Handler();

    private boolean isActivityAuthenticationStarted = false;

    //This is to update the time on real time
    private final BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    updateTime();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This is to hide the status and navigation bar
        //however it can be reveal by an inward swipe along the region where
        //the status and navigation bar normally appear
        HideStatusAndNavigationBar hideStatusAndNavigationBar = new HideStatusAndNavigationBar(this);
        hideStatusAndNavigationBar.hideStatusAndNavigationBar();

        setContentView(R.layout.activity_splash_screen);

        imageButton = (FloatingActionButton) findViewById(R.id.btnNavigationToLogin);
        imageButton.setOnClickListener(this);

        txtViewCurrentDate = (TextView) findViewById(R.id.txtCurrentDate);
        txtViewCurrentTime = (TextView) findViewById(R.id.txtCurrentTime);

        //declaring intent filter to notify the time change on real time
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(intentReceiver, intentFilter);

        updateTime();

         new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isActivityAuthenticationStarted == false){
                    loadMainActivity();

                    finish();
                }
            }
        }, Constants.SPLASH_TIME_OUT);

    }


    private void updateTime() {
        //String mobileTime = DateAndTimeHelper.getMobileTime();
        //String mobileDate = DateAndTimeHelper.getMobileDate();

        txtViewCurrentTime.setText(DateAndTimeHelper.getCurrentTime());
        txtViewCurrentDate.setText(DateAndTimeHelper.getFormattedCurrentDate());
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(intentReceiver);

        super.onDestroy();
    }

    @Override
    protected void onResume() {

        //This is to hide the status and navigation bar
        //however it can be reveal by an inward swipe along the region where
        //the status and navigation bar normally appear
        HideStatusAndNavigationBar hideStatusAndNavigationBar = new HideStatusAndNavigationBar(this);
        hideStatusAndNavigationBar.hideStatusAndNavigationBar();

        super.onResume();
    }

    @Override
    public void onClick(View v) {
        isActivityAuthenticationStarted = true;

        loadMainActivity();

        finish();
    }

    public void loadMainActivity(){
        //Reading the shared preference of "Remember Me"
        //if the user selected "Remember Me" when login,
        //it will keep load the "All Account" activity instead of Login
        //until user manually "Logout" from the menu
        MySharedPrefs mySharedPrefs = new MySharedPrefs(getApplicationContext());
        String rememberMeFlag = mySharedPrefs.getDataFromSharePrefs(mySharedPrefs.PREFS_KEY_FOR_REMEMBER_ME);
        rememberMeFlag = rememberMeFlag.trim().toLowerCase();
        if ((rememberMeFlag != null) && (rememberMeFlag.equals("true"))) {
            startActivity(new Intent(SplashScreenActivity.this, AllAccountsActivity.class));
        }else{
            startActivity(new Intent(SplashScreenActivity.this, AuthenticationViewPageFragementActivity.class));
        }
    }
}
