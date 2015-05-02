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
import com.bobcode.splitexpense.utils.MyUtils;


public class ForgotCredentialActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private Spinner spinnerForgotCredUserName;

    private EditText editViewForgotCredAnswer;

    private TextView textViewRevelPassword;

    private ImageButton fabtnForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_credential);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Forgot Credential");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //spinner to select user name
        spinnerForgotCredUserName = (Spinner) findViewById(R.id.spinnerForgotCredUserName);
        String[] registeredUsers = getResources().getStringArray(R.array.userTemp);
        ArrayAdapter<String> adapterForRegisteredUsers = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.textViewSpinner, registeredUsers);
        adapterForRegisteredUsers.setDropDownViewResource(R.layout.spinner_item);
        spinnerForgotCredUserName.setAdapter(adapterForRegisteredUsers);

        //answer for the security question
        editViewForgotCredAnswer = (EditText) findViewById(R.id.editViewForgotCredAnswer);

        //to revel the password to user
        textViewRevelPassword = (TextView) findViewById(R.id.textViewRevelPassword);

        //fb btn for OK
        fabtnForgotPassword = (ImageButton) findViewById(R.id.fabtnForgotPassword);
        fabtnForgotPassword.setOnClickListener(this);
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
                String answer = editViewForgotCredAnswer.getText().toString().trim();
                if (answer.isEmpty()) {
                    MyUtils.showToast(this, "enter answer to revel the password");
                    editViewForgotCredAnswer.setFocusable(true);
                    editViewForgotCredAnswer.requestFocus();
                } else {
//--------------------------- pending -----------------------------------------------------//
                    //edit field to enter the security question answer
                    //upon validating the answer for the selected user from the "user_profile" table
                    // if answer matches, revel the password else
                    // show toast message - "incorrect answer"
                    textViewRevelPassword.setText("password_reveled");
                    textViewRevelPassword.setVisibility(View.VISIBLE);
                    fabtnForgotPassword.setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callBackPressed();
                        }
                    }, 1000);
//--------------------------- pending -----------------------------------------------------//
                }
                break;
        }
    }

    public void callBackPressed() {
        super.onBackPressed();

        MyUtils.myPendingTransitionLeftInRightOut(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        MyUtils.myPendingTransitionLeftInRightOut(this);
    }
}
