package com.bobcode.splitexpense.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.fragments.LoginFragment;
import com.bobcode.splitexpense.fragments.RegisterFragment;

/**
 * Created by vijayananjalees on 4/2/15.
 */
public class MyFragmentPageAdapter extends FragmentPagerAdapter {

    private Context context;

    private static final int TOTAL_PAGE = 2;

    private String tabTitles[] = new String[]{"Login", "Register"};

    private int iconsResID[] = {R.drawable.ic_tab_login, R.drawable.ic_tab_register};

    public MyFragmentPageAdapter(FragmentManager fm, Context content) {
        super(fm);
        this.context = content;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return LoginFragment.newInstance(0, "Page 1");

            case 1:
                return RegisterFragment.newInstance(1, "Page 2");

            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return tabTitles[position];
        Drawable image = context.getResources().getDrawable(iconsResID[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        //SpannableString sb = new SpannableString(" " + tabTitles[position]);
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    @Override
    public int getCount() {
        return TOTAL_PAGE;
    }
}
