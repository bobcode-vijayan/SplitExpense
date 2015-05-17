package com.bobcode.splitexpense.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.helpers.SplitExpenseSQLiteHelper;
import com.bobcode.splitexpense.models.UserProfileTableModel;
import com.bobcode.splitexpense.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

//--------------------------------------------------------------------------------//
//take the user to forgot credential activity
//spinner to select a user name from all registered user
//edit field to enter the security question answer
//upon validating the answer, revel the password
//--------------------------------------------------------------------------------//

public class ForgotCredentialActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private TextView textViewForgotCredUseName;

    private Spinner spinnerForgotCredUserName;

    private TextView textViewForgotCredQuestionLabel;

    private EditText editViewForgotCredAnswer;

    private TextView textViewRevelPassword;

    private ImageButton fabtnForgotPassword;

    private SplitExpenseSQLiteHelper splitExpenseSQLiteHelper;

    private List<UserProfileTableModel> allRegisteredUser;

    private boolean isRegisteredUserFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_credential);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Forgot Credential");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //label for user name
        textViewForgotCredUseName = (TextView) findViewById(R.id.textViewForgotCredUseName);

        //spinner to select user name
        spinnerForgotCredUserName = (Spinner) findViewById(R.id.spinnerForgotCredUserName);

        //label for the security question
        textViewForgotCredQuestionLabel = (TextView) findViewById(R.id.textViewForgotCredQuestionLabel);

        //answer for the security question
        editViewForgotCredAnswer = (EditText) findViewById(R.id.editViewForgotCredAnswer);

        //to revel the password to user
        textViewRevelPassword = (TextView) findViewById(R.id.textViewRevelPassword);

        //fb btn for OK
        fabtnForgotPassword = (ImageButton) findViewById(R.id.fabtnForgotPassword);
        fabtnForgotPassword.setOnClickListener(this);

        //populate spinner with all the registered user from "user_profile" table
        allRegisteredUser = new ArrayList<UserProfileTableModel>();
        splitExpenseSQLiteHelper = new SplitExpenseSQLiteHelper(this);
        allRegisteredUser = splitExpenseSQLiteHelper.getAllRegisteredUser();
        List<String> userNames = new ArrayList<String>();
        int registerUsersCount = allRegisteredUser.size();
        if (registerUsersCount != 0) {
            isRegisteredUserFound = true;

            for (UserProfileTableModel currentRegisteredUser : allRegisteredUser) {
                String currentUserName = currentRegisteredUser.getUserName().trim();
                userNames.add(currentUserName);
            }
            ArrayAdapter<String> adapterForRegisteredUsers = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.textViewSpinner, userNames);
            adapterForRegisteredUsers.setDropDownViewResource(R.layout.spinner_item);
            spinnerForgotCredUserName.setAdapter(adapterForRegisteredUsers);
        } else {
            isRegisteredUserFound = false;

            textViewForgotCredUseName.setVisibility(View.INVISIBLE);
            spinnerForgotCredUserName.setVisibility(View.INVISIBLE);
            textViewForgotCredQuestionLabel.setVisibility(View.INVISIBLE);
            editViewForgotCredAnswer.setVisibility(View.INVISIBLE);

            textViewRevelPassword.setText("No registered user found");
            textViewRevelPassword.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_credential, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                MyUtils.myPendingTransitionLeftInRightOut(this);

                break;

            default:
                //add account, add member, report, help & logout handled here
                MyUtils.commonMenuActions(this, item);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtnForgotPassword:
                if (isRegisteredUserFound) {
                    //user entered answer
                    String answer = editViewForgotCredAnswer.getText().toString().trim();
                    if (answer.isEmpty()) {
                        MyUtils.showToast(this, "enter answer to revel the password");
                        editViewForgotCredAnswer.setFocusable(true);
                        editViewForgotCredAnswer.requestFocus();
                    } else {
                        //edit field to enter the security question answer
                        //upon validating the answer for the selected user from the "user_profile" table
                        // if answer matches, revel the password else
                        // show toast message - "incorrect answer"

                        //user selected user name
                        String selectedUserName = spinnerForgotCredUserName.getSelectedItem().toString().trim();
                        int registerUsersCount = allRegisteredUser.size();
                        if (registerUsersCount != 0) {
                            for (UserProfileTableModel currentRegisteredUser : allRegisteredUser) {
                                String currentUserName = currentRegisteredUser.getUserName().trim();
                                String currentAnswer = currentRegisteredUser.getAnswer().trim();
                                if(selectedUserName.equals(currentUserName)){
                                    if(answer.equals(currentAnswer)){
                                        textViewRevelPassword.setText(currentRegisteredUser.getPassword());
                                        textViewRevelPassword.setVisibility(View.VISIBLE);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                onBackPressed();
                                            }
                                        }, 1000);
                                    }else{
                                        MyUtils.showToast(this, "Answer does not match. Please try again");
                                    }
                                }
                            }
                        }
                    }
                } else {
                    onBackPressed();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        MyUtils.myPendingTransitionLeftInRightOut(this);
    }
}
