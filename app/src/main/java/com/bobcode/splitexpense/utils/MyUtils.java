package com.bobcode.splitexpense.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.activities.AddOREditAccountActivity;
import com.bobcode.splitexpense.activities.AllMembersActivity;
import com.bobcode.splitexpense.activities.AuthenticationViewPageFragementActivity;
import com.bobcode.splitexpense.constants.Constants;

/**
 * Created by vijayananjalees on 4/15/15.
 */
public final class MyUtils extends Application {


    public static final boolean minCharCheck(String value, int minChar) {

        if (value.length() >= minChar)
            return true;

        return false;
    }

    //this function is to show a toast message
    public static final void showToast(Context context, String message) {
        final Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1000);
    }

    public static final void commonMenuActions(Context context, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_help:
                break;

            case R.id.action_logout:
                MySharedPrefs mySharedPrefs = new MySharedPrefs(context.getApplicationContext());
                mySharedPrefs.storeDataToSharePrefs(mySharedPrefs.PREFS_KEY_FOR_REMEMBER_ME, "false");
                context.startActivity(new Intent(context.getApplicationContext(), AuthenticationViewPageFragementActivity.class));

                break;

            case R.id.action_add_account:
                MyUtils.showToast(context, "Adding account");

                Intent intentAddAccount = new Intent(context.getApplicationContext(), AddOREditAccountActivity.class);
                intentAddAccount.putExtra(Constants.ACCOUNT_ACTION, "ADD");
                intentAddAccount.putExtra(Constants.ADD_ACCOUNT_ACTION_SOURCE, "MENUADDACCOUNT");
                context.startActivity(intentAddAccount);

                break;

            case R.id.action_members:
                MyUtils.showToast(context, "Viewing all members");

                Intent intentAllMember = new Intent(context.getApplicationContext(), AllMembersActivity.class);
                context.startActivity(intentAllMember);
                break;
        }
    }


    //this function will dynamically load the check box for each mebers
    public static CheckBox[] loadMembersCheckbox(GridLayout gridLayout, CheckBox[] checkBox, String[] members, Context context) {
        //setting the layoutparam to arrange the members checkbox
        RelativeLayout.LayoutParams chkBoxParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 130);

        int totalMembers = members.length;

        checkBox = new CheckBox[totalMembers];
        for (int index = 0; index < totalMembers; index++) {
            checkBox[index] = new CheckBox(context);
            checkBox[index].setText(members[index]);
            checkBox[index].setId(index);
            checkBox[index].setLayoutParams(chkBoxParams);
            checkBox[index].setTextColor(context.getResources().getColor(R.color.primary_text));
            gridLayout.addView(checkBox[index]);
        }
        return checkBox;
    }

    public static void myPendingTransitionRightInLeftOut(Activity activity) {
        activity.overridePendingTransition(R.anim.enter_right_in, R.anim.exit_left_out);
    }

    public static void myPendingTransitionLeftInRightOut(Activity activity) {
        activity.overridePendingTransition(R.anim.enter_left_in, R.anim.exit_right_out);
    }
}
