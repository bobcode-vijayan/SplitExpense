package com.bobcode.splitexpense.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.activities.AllAccountsActivity;
import com.bobcode.splitexpense.activities.ForgotCredentialActivity;
import com.bobcode.splitexpense.constants.Constants;
import com.bobcode.splitexpense.utils.MySharedPrefs;
import com.bobcode.splitexpense.utils.MyUtils;

/**
 * Created by vijayananjalees on 4/2/15.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private int pageNumber;
    private String pageName;

    private EditText editTxtLoginUserName;
    private EditText editTxtLoginPassword;

    private CheckBox checkBoxRememberMe;

    private Button btnLogin;
    private Button btnLoginClear;

    private TextView txtViewForgotCredentials;

    private boolean isLoginUserNameValid;
    private boolean isLoginPasswordValid;

    private MySharedPrefs mySharedPrefs;

    private boolean isLoginValidationSuccess;

    public static LoginFragment newInstance(int pageNumber, String pageName) {
        LoginFragment loginFragment = new LoginFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("pageNumber", pageNumber);
        bundle.putString("pageName", pageName);

        loginFragment.setArguments(bundle);
        return loginFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt("pageNumber", 0);
        pageName = getArguments().getString("pageName");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //user name field
        editTxtLoginUserName = (EditText) view.findViewById(R.id.editTxtLoginUserName);

        //password field
        editTxtLoginPassword = (EditText) view.findViewById(R.id.editTxtLoginPassword);

        //Remember me checkbox
        checkBoxRememberMe = (CheckBox) view.findViewById(R.id.checkBoxRememberMe);

        //retrieve "remember me" preference and set the last login user name
        //if the remember me flag is true
        mySharedPrefs = new MySharedPrefs(getActivity());
        String rememberMeFlag = mySharedPrefs.getDataFromSharePrefs(mySharedPrefs.PREFS_KEY_FOR_REMEMBER_ME);
        rememberMeFlag = rememberMeFlag.trim().toLowerCase();
        if ((rememberMeFlag != null) && (rememberMeFlag.equals("true"))) {
            editTxtLoginUserName.setText(mySharedPrefs.getDataFromSharePrefs(mySharedPrefs.PREFS_KEY_FOR_USERNAME));
            checkBoxRememberMe.setChecked(true);
        }


        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLoginClear = (Button) view.findViewById(R.id.btnLoginClear);

        txtViewForgotCredentials = (TextView) view.findViewById(R.id.txtViewLoginForgotCredentials);

        btnLogin.setOnClickListener(this);
        btnLoginClear.setOnClickListener(this);
        txtViewForgotCredentials.setOnClickListener(this);

        editTxtLoginPassword.setOnTouchListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.txtViewLoginForgotCredentials:
                txtViewForgotCredentials.setTextColor(getResources().getColor(R.color.accent));
//--------------------------- pending -----------------------------------------------------//
                //take the user to forgot credential activity
                //spinner to select a user name from all registered user
                //edit field to enter the security question answer
                //upon validating the answer, revel the password

                Intent intentForgotCredential = new Intent(getActivity(), ForgotCredentialActivity.class);
                startActivity(intentForgotCredential);
//--------------------------- pending -----------------------------------------------------//

                break;

            case R.id.btnLoginClear:
                editTxtLoginUserName.setText("");
                editTxtLoginPassword.setText("");

                //after clear user name & password, set the focus to user name field
                editTxtLoginUserName.setFocusable(true);
                editTxtLoginUserName.requestFocus();
                break;

            case R.id.btnLogin:
                //read the user input
                String strSignUpUserName = editTxtLoginUserName.getText().toString().trim();
                String strSignUpPassword = editTxtLoginPassword.getText().toString().trim();

                //validate user name min char
                if (!MyUtils.minCharCheck(strSignUpUserName, Constants.USERNAME_MIN_LENGTH)) {
                    MyUtils.showToast(getActivity(), "user name minimum char should be 4");
                    isLoginUserNameValid = false;

                    //if minimum char not met, set the focus to user name field
                    editTxtLoginUserName.setFocusable(true);
                    editTxtLoginUserName.requestFocus();
                } else {
                    isLoginUserNameValid = true;
                }

                //validate password min char
                if (!MyUtils.minCharCheck(strSignUpPassword, Constants.PASSWORD_MIN_LENGTH)) {
                    MyUtils.showToast(getActivity(), "password minimum char should be 6");
                    isLoginPasswordValid = false;

                    if (isLoginUserNameValid) {
                        editTxtLoginPassword.setFocusable(true);
                        editTxtLoginPassword.requestFocus();
                    }
                } else {
                    isLoginPasswordValid = true;
                }

                if (isLoginUserNameValid && isLoginPasswordValid) {
                    //retrieve registered user's data from "user_profile" table
//--------------------------- pending -----------------------------------------------------//
                    //field to retrieve 1) user name
                    //              2) password
                    //validate against entered user name and password
                    //action based on validation
                    //strSignUpUserName.equals(username-"user_profile") && strSignUpPassword.equals(password-"user_profile")
                    isLoginValidationSuccess = true;
//--------------------------- pending -----------------------------------------------------//
                    if (isLoginValidationSuccess) {
                        //validate whether user selected "remember me" checkbox
                        //if it is selected, store the "user name" and "remember me" in shared preference
                        if (checkBoxRememberMe.isChecked()) {
                            mySharedPrefs.storeDataToSharePrefs(mySharedPrefs.PREFS_KEY_FOR_REMEMBER_ME, "true");
                            mySharedPrefs.storeDataToSharePrefs(mySharedPrefs.PREFS_KEY_FOR_USERNAME, strSignUpUserName);
                        } else {
                            mySharedPrefs.storeDataToSharePrefs(mySharedPrefs.PREFS_KEY_FOR_REMEMBER_ME, "false");
                            mySharedPrefs.storeDataToSharePrefs(mySharedPrefs.PREFS_KEY_FOR_USERNAME, "");
                        }

                        Intent intent = new Intent(getActivity(), AllAccountsActivity.class);
                        startActivity(intent);
                        MyUtils.myPendingTransitionRightInLeftOut(getActivity());

                        //remove the authentication view pager fragment activity
                        getActivity().finish();

                    } else {
                        MyUtils.showToast(getActivity(), "user name or password is invalid");
                    }
                }
                break;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            case R.id.editTxtLoginPassword:
                showKeyboard(editTxtLoginPassword);

                //read the user input
                String strSignUpUserName = editTxtLoginUserName.getText().toString().trim();

                //validate user name min char
                if (strSignUpUserName.isEmpty()) {
                    MyUtils.showToast(getActivity(), "enter use name before password");
                } else if (!MyUtils.minCharCheck(strSignUpUserName, Constants.USERNAME_MIN_LENGTH)) {
                    MyUtils.showToast(getActivity(), "user name minimum char should be 4");
                } else {
                    editTxtLoginPassword.setFocusable(true);
                    editTxtLoginPassword.requestFocus();
                }

                return true;
        }

        return true;
    }

    public final void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }
}
