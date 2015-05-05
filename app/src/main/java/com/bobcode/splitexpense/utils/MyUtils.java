package com.bobcode.splitexpense.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;

/**
 * Created by vijayananjalees on 4/15/15.
 */

//public final class MyUtils extends Application {
public final class MyUtils {

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
        }, 1300);
    }

    public static final void commonMenuActions(Context context, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_help:
                break;

            case R.id.action_exit:
                exitApp(context);
                break;

            case R.id.action_logout:
                //Set the "Remember Me" prefs false to ensure app will show authentication activity
                //if user open the app next time
                MySharedPrefs mySharedPrefs = new MySharedPrefs(context.getApplicationContext());
                mySharedPrefs.storeDataToSharePrefs(mySharedPrefs.PREFS_KEY_FOR_REMEMBER_ME, "false");

                //This is to clear all the task
                Intent intent = new Intent(context.getApplicationContext(), AuthenticationViewPageFragementActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                MyUtils.myPendingTransitionRightInLeftOut((Activity) context);
                break;

            case R.id.action_add_account:
                MyUtils.showToast(context, "Adding account");

                Intent intentAddAccount = new Intent(context.getApplicationContext(), AddOREditAccountActivity.class);
                intentAddAccount.putExtra(Constants.ACCOUNT_ACTION, "ADD");
                intentAddAccount.putExtra(Constants.ADD_ACCOUNT_ACTION_SOURCE, "MENUADDACCOUNT");
                context.startActivity(intentAddAccount);
                MyUtils.myPendingTransitionRightInLeftOut((Activity)context);
                break;

            case R.id.action_members:
                MyUtils.showToast(context, "Viewing all members");

                Intent intentAllMember = new Intent(context.getApplicationContext(), AllMembersActivity.class);
                context.startActivity(intentAllMember);
                MyUtils.myPendingTransitionRightInLeftOut((Activity) context);
                break;
        }
    }

    public static void exitApp(Context context) {
        //Android's design does not favor exiting an application by choice, but rather manages it by the OS.
        //We can bring up the Home application by its corresponding Intent:

        //Method-I
//                Activity activity = (Activity) context;
//                activity.moveTaskToBack(true);

        //Method-II
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getPhoto(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
