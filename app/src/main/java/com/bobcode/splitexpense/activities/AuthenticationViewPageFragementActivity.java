package com.bobcode.splitexpense.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.adapters.MyFragmentPageAdapter;
import com.bobcode.splitexpense.interfaces.Communicator;
import com.bobcode.splitexpense.tablayouts.SlidingTabLayout;

/**
 * Created by vijayananjalees on 4/14/15.
 */
public class AuthenticationViewPageFragementActivity extends FragmentActivity implements Communicator {

    private FragmentPagerAdapter fragmentPagerAdapter;
    private ViewPager viewPager;

    private SlidingTabLayout slidingTabLayout;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_viewpager);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Split Expense");

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragmentPagerAdapter = new MyFragmentPageAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(fragmentPagerAdapter);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidingTabLayout);
        //this is to set the custom textview in the tab
        slidingTabLayout.setCustomTabView(R.layout.custom_tab, 0);

        //this is to give equal space(weight) for a the tabs
        slidingTabLayout.setDistributeEvenly(true);

        slidingTabLayout.setViewPager(viewPager);

        //this is to customize the tab indication color
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.accent);
            }
        });
//--------------------------- pending -----------------------------------------------------//
        //check if any register user. If any registered user in "userprofile" table
        //set the viewPager to 0th item (Login Page)
        //if no registered user found, set the viewPager to 1th item(Registration page)
        viewPager.setCurrentItem(1);
//--------------------------- pending -----------------------------------------------------//

    }

    @Override
    public void registeredUserName(String userName) {
        //Setting the user name upon successful completion of user registration
        EditText editTxtLoginUserName = (EditText) findViewById(R.id.editTxtLoginUserName);
        editTxtLoginUserName.setText(userName);
    }
}
