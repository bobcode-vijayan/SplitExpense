package com.bobcode.splitexpense.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.interfaces.Communicator;
import com.bobcode.splitexpense.constants.Constants;
import com.bobcode.splitexpense.utils.MySharedPrefs;
import com.bobcode.splitexpense.utils.MyUtils;

/**
 * Created by vijayananjalees on 4/2/15.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private int pageNumber;
    private String pageName;

    private Button btnSignUpClear;
    private Button btnSignUp;

    private EditText editTxtSignUpUserName;
    private EditText editTxtSignUpPassword;
    private EditText editTxtSignUpConfirmPassword;
    private EditText editTxtSignUpAnswer;

    private boolean isSingUpUserNameValid;
    private boolean isSignUpPasswordValid;
    private boolean isSignUpAnswerValid;
    private boolean isConfirmPasswordMatch;

    private MySharedPrefs mySharedPrefs;

    private Communicator communicator;

    public static RegisterFragment newInstance(int pageNumber, String pageName) {
        RegisterFragment registerFragment = new RegisterFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("pageNumber", pageNumber);
        bundle.putString("pageName", pageName);

        registerFragment.setArguments(bundle);
        return registerFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            communicator = (Communicator) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement registeredUserName");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt("pageNumber", 0);
        pageName = getArguments().getString("pageName");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        editTxtSignUpUserName = (EditText) view.findViewById(R.id.editTxtSignUpUserName);
        editTxtSignUpPassword = (EditText) view.findViewById(R.id.editTxtSignUpPassword);
        editTxtSignUpConfirmPassword = (EditText) view.findViewById(R.id.editTxtSignUpConfirmPassword);
        editTxtSignUpAnswer = (EditText) view.findViewById(R.id.editTxtSignUpAnswer);

        btnSignUp = (Button) view.findViewById(R.id.btnSignUp);
        btnSignUpClear = (Button) view.findViewById(R.id.btnSignUpClear);

        editTxtSignUpPassword.setOnTouchListener(this);
        editTxtSignUpConfirmPassword.setOnTouchListener(this);
        editTxtSignUpAnswer.setOnTouchListener(this);

        btnSignUp.setOnClickListener(this);
        btnSignUpClear.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        //read the user input
        final String sSignUpUserName = editTxtSignUpUserName.getText().toString().trim();
        final String sSignUpPassword = editTxtSignUpPassword.getText().toString().trim();
        final String sSignUpConfirmPassword = editTxtSignUpConfirmPassword.getText().toString().trim();
        final String sSecurityAnswer = editTxtSignUpAnswer.getText().toString().trim();

        switch (v.getId()) {
            case R.id.btnSignUpClear:
                //clear all the editbox value if user click the clear button
                editTxtSignUpUserName.setText("");
                editTxtSignUpPassword.setText("");
                editTxtSignUpConfirmPassword.setText("");
                editTxtSignUpAnswer.setText("");

                //set the focus to user name field
                editTxtSignUpUserName.setFocusable(true);
                editTxtSignUpUserName.requestFocus();
                break;

            case R.id.btnSignUp:

                //validate user name min char if not show the toast message
                //user name must be >=4
                if (!MyUtils.minCharCheck(sSignUpUserName, Constants.USERNAME_MIN_LENGTH)) {
                    MyUtils.showToast(getActivity(), "user name must contain at least 4 characters!");
                    isSingUpUserNameValid = false;
                } else {
                    isSingUpUserNameValid = true;
                }

                //validate password min char if not show the toast message
                //password must be >=6
                if (!MyUtils.minCharCheck(sSignUpPassword, Constants.PASSWORD_MIN_LENGTH)) {
                    MyUtils.showToast(getActivity(), "password must contain at least 6 characters!");
                    isSignUpPasswordValid = false;
                } else {
                    isSignUpPasswordValid = true;
                }

                //validating confirm password min char is not required since
                //validating "confirm password" is equal to "password" will redundant the min char validation
                //validate confirm password match with password
                if (!sSignUpConfirmPassword.equals(sSignUpPassword)) {
                    MyUtils.showToast(getActivity(), "password and confirmation password do not match");

                    editTxtSignUpConfirmPassword.setFocusable(true);
                    editTxtSignUpConfirmPassword.requestFocus();

                    isConfirmPasswordMatch = false;
                } else {
                    isConfirmPasswordMatch = true;
                }

                //validate answer field -- if field is empty show the toast message
                if (sSecurityAnswer.equals("")) {
                    MyUtils.showToast(getActivity(), "answer field should not be empty");

                    editTxtSignUpAnswer.setFocusable(true);
                    editTxtSignUpAnswer.requestFocus();

                    isSignUpAnswerValid = false;
                } else {
                    isSignUpAnswerValid = true;
                }

                if (isSingUpUserNameValid && isSignUpPasswordValid && isSignUpAnswerValid && isConfirmPasswordMatch) {
                    //store the data in "user_profile" table
//--------------------------- pending -----------------------------------------------------//
                    //upon successful sign-up, save the user detail to database
                    //field to save 1) user name
                    //              2) password
                    //              3) answer
//--------------------------- pending -----------------------------------------------------//

                    //calling the interface to pass the registered user name to Login Fragment
                    communicator.registeredUserName(sSignUpUserName);

                    //upon successful sign-up, take the user to viewPager 0th item (Login Page)
                    ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewPager);
                    viewPager.setCurrentItem(0);
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //read the user input
        final String sSignUpUserName = editTxtSignUpUserName.getText().toString().trim();
        final String sSignUpPassword = editTxtSignUpPassword.getText().toString().trim();
        final String sSignUpConfirmPassword = editTxtSignUpConfirmPassword.getText().toString().trim();
        final String sSecurityAnswer = editTxtSignUpAnswer.getText().toString().trim();

        switch (v.getId()) {

            case R.id.editTxtSignUpPassword:
                showKeyboard(editTxtSignUpPassword);

                if (sSignUpUserName.isEmpty()) {
                    MyUtils.showToast(getActivity(), "enter user name before password");
                    editTxtSignUpUserName.setFocusable(true);
                    editTxtSignUpUserName.requestFocus();
                } else if (!MyUtils.minCharCheck(sSignUpUserName, Constants.USERNAME_MIN_LENGTH)) {
                    MyUtils.showToast(getActivity(), "user name must contain at least 4 characters!");
                    editTxtSignUpUserName.setFocusable(true);
                    editTxtSignUpUserName.requestFocus();
                } else {
                    return false;
                }
                return true;


            case R.id.editTxtSignUpConfirmPassword:
                if (sSignUpPassword.isEmpty()) {
                    MyUtils.showToast(getActivity(), "enter password before confirm password");
                    editTxtSignUpPassword.setFocusable(true);
                    editTxtSignUpPassword.requestFocus();

                } else if (!MyUtils.minCharCheck(sSignUpPassword, Constants.PASSWORD_MIN_LENGTH)) {
                    MyUtils.showToast(getActivity(), " password must contain at least 6 characters!");
                    editTxtSignUpPassword.setFocusable(true);
                    editTxtSignUpPassword.requestFocus();
                } else {
                    showKeyboard(editTxtSignUpConfirmPassword);

                    return false;
                }

                return true;

            case R.id.editTxtSignUpAnswer:
                if (!sSignUpPassword.equals(sSignUpConfirmPassword)) {
                    MyUtils.showToast(getActivity(), "password and confirmation password do not match");
                    editTxtSignUpConfirmPassword.setFocusable(true);
                    editTxtSignUpConfirmPassword.requestFocus();
                } else {
                    return false;
                }
                return true;
        }

        return false;
    }

    public final void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

}
