package com.bobcode.splitexpense.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.constants.Constants;
import com.bobcode.splitexpense.utils.MyUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddOREditAccountActivity extends ActionBarActivity implements View.OnClickListener {
    private Toolbar toolbar;

    private EditText editViewAEAAccountName;
    private AutoCompleteTextView textViewAEADescriptionAutoComplete;

    private Spinner spinnerAEACurrency;
    private Spinner spinnerAEAStatus;

    private TextView textViewAEACheckAll;

    private ImageButton imgAABtnOk;
    private ImageButton imgAABtnCancel;

    private GridLayout gridLayoutAEAMember;

    private CheckBox[] checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_account);

        //This is to auto complete text for account description
        //when ever user create an account the description should stored in preferences
        //and the entry should be loaded to auto complete list dynamically
        String[] accountDescriptionAutoList = getResources().getStringArray(R.array.accountDescriptionAutoList);

        //This is a static supported currency list
        //this data is stored in <string-array name="currencyList">
        final String[] currencyListArray = getResources().getStringArray(R.array.currencyList);

        //This is a static account status
        //it might be New, Active, Pending or Settled
        //this data is stored in <string-array name="accountStatus">
        final String[] accountStatusArray = getResources().getStringArray(R.array.accountStatus);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.drawable.ic_menu_add_edit_account);

        //Account Name
        editViewAEAAccountName = (EditText) findViewById(R.id.editViewAEAAccountName);

        //Account description auto complete
        textViewAEADescriptionAutoComplete = (AutoCompleteTextView) findViewById(R.id.textViewAEADescriptionAutoComplete);
        //Description autocomplete
        //When ever user create an account the description will be saved in preference
        //When a user create a new account and while entering description, user will get the
        //all saved description from preference to select to avoid typing
        //ArrayAdapter<String> arrayAdapterForAutoComplete = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.textViewSpinner, accountDescriptionAutoList);
        ArrayAdapter<String> arrayAdapterForAutoComplete = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accountDescriptionAutoList);
        textViewAEADescriptionAutoComplete.setAdapter(arrayAdapterForAutoComplete);
        textViewAEADescriptionAutoComplete.setThreshold(1);

        //currency spinner
        spinnerAEACurrency = (Spinner) findViewById(R.id.spinnerAEACurrency);
        ArrayAdapter<String> arrayAdapterForCurrency = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.textViewSpinner, currencyListArray);
        arrayAdapterForCurrency.setDropDownViewResource(R.layout.spinner_item);
        spinnerAEACurrency.setAdapter(arrayAdapterForCurrency);

        //account status spinner
        spinnerAEAStatus = (Spinner) findViewById(R.id.spinnerAEAStatus);
        ArrayAdapter<String> arrayAdapterForAccountStatus = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.textViewSpinner, accountStatusArray);
        arrayAdapterForAccountStatus.setDropDownViewResource(R.layout.spinner_item);
        spinnerAEAStatus.setAdapter(arrayAdapterForAccountStatus);

//**************************************** Dynamically add check box for members ***************************
        //Add account member dynamically
        //now it is created based on static string array
        //later it should populate from database from members table

        //only connect to database is pending

        //grid layout to add the members checkbox
        gridLayoutAEAMember = (GridLayout) findViewById(R.id.gridLayoutAEAMember);

        //members list -- this data should come from database table members
        String members[] = getResources().getStringArray(R.array.membersTemp);
        int totalMembers = (int) members.length;
        int noOfRows = 0;
        if ((totalMembers) % 2 >= 1) {
            noOfRows = (totalMembers / 2) + 1;
        } else {
            noOfRows = (totalMembers / 2);
        }
        gridLayoutAEAMember.setRowCount(noOfRows);
        gridLayoutAEAMember.setColumnCount(2);

        //dynamically load the check for each members
        checkBox = MyUtils.loadMembersCheckbox(gridLayoutAEAMember, checkBox, members, this);
//**************************************** Dynamically add check box for members ***************************


        //reading the data passed by intent to add an account
        String accountAction = getIntent().getStringExtra(Constants.ACCOUNT_ACTION);
        //if the data is not null, convert it to lower case
        if (accountAction != null)
            accountAction = accountAction.toLowerCase();

        if (accountAction.equals("edit")) {
            toolbar.setTitle("Edit Account");

            String accountName = getIntent().getStringExtra(Constants.ACCOUNT_NAME);
            String accountDescription = getIntent().getStringExtra(Constants.ACCOUNT_DESCRIPTION);
            String accountCurrency = getIntent().getStringExtra(Constants.ACCOUNT_CURRENCY);
            String accountStatus = getIntent().getStringExtra(Constants.ACCOUNT_STATUS);
            String accountMembers = getIntent().getStringExtra(Constants.ACCOUNT_MEMBERS);

            //set the account with the selected account name
            editViewAEAAccountName.setText(accountName);

            //set the account description with the selected account description
            textViewAEADescriptionAutoComplete.setText(accountDescription);

            //selecting the appropriate currency from the spinner based on selected account information
            //Method-I
//            accountCurrency = accountCurrency.toLowerCase();
//            for(int index=0; index < spinnerAEACurrency.getAdapter().getCount(); index++){
//                String currentIndexSpinnerItem = spinnerAEACurrency.getItemAtPosition(index).toString().toLowerCase();
//
//                if(accountCurrency.equals(currentIndexSpinnerItem)){
//                    spinnerAEACurrency.setSelection(index);
//                    break;
//                }
//            }

            //Method-II
            spinnerAEACurrency.setSelection(Arrays.asList(currencyListArray).indexOf(accountCurrency));

            //selecting the appropriate status from the spinner based on selected account information
            accountStatus = accountStatus.toLowerCase();
            for (int index = 0; index < spinnerAEAStatus.getAdapter().getCount(); index++) {
                String currentIndexSpinnerItem = spinnerAEAStatus.getItemAtPosition(index).toString().toLowerCase();

                if (accountStatus.equals(currentIndexSpinnerItem)) {
                    spinnerAEAStatus.setSelection(index);
                    break;
                }
            }

            //pre-selected the members from the selected account information
            String[] selectedAccountMembers = accountMembers.split(",");
            for (int indexAccountMembers = 0; indexAccountMembers < selectedAccountMembers.length; indexAccountMembers++) {
                String memberTemp = selectedAccountMembers[indexAccountMembers].trim().toLowerCase();
                for (int indexCheckBox = 0; indexCheckBox < checkBox.length; indexCheckBox++) {
                    String checkBoxMemberName = checkBox[indexCheckBox].getText().toString().trim().toLowerCase();
                    if (checkBoxMemberName.equals(memberTemp)) {
                        checkBox[indexCheckBox].setChecked(true);
                        break;
                    }
                }
            }
        } else if (accountAction.equals("add")) {
            toolbar.setTitle("Add Account");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //single click to  Check All members
        textViewAEACheckAll = (TextView) findViewById(R.id.textViewAEACheckAll);
        textViewAEACheckAll.setOnClickListener(this);

        imgAABtnOk = (ImageButton) findViewById(R.id.imgAEABtnOk);
        imgAABtnOk.setOnClickListener(this);

        imgAABtnCancel = (ImageButton) findViewById(R.id.imgAEABtnCancel);
        imgAABtnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.textViewAEACheckAll:
                for (int index = 0; index < checkBox.length; index++) {
                    checkBox[index].setChecked(true);
                }
                break;

            case R.id.imgAEABtnOk:
                //this the tick(Green color) button to create/edit the account with the user input

                String members[] = getResources().getStringArray(R.array.membersTemp);
                List<String> selectedMembers = new ArrayList<String>();
                for (int index = 0; index < members.length; index++) {
                    if (checkBox[index].isChecked()) {
                        selectedMembers.add(checkBox[index].getText().toString());
                    }
                }
                //this is to close the add/edit account activity and will take the user to All Account activity
//                Intent intentOKAllAccount = new Intent(this, AllAccountsActivity.class);
//                startActivity(intentOKAllAccount);
//                MyUtils.myPendingTransitionLeftInRightOut(this);

                callAllAccountActivity("ok");

                break;

            case R.id.imgAEABtnCancel:
                //this is to close the add/edit account activity and will take the user to All Account activity
//                Intent intentCancelAllAccount = new Intent(this, AllAccountsActivity.class);
//                startActivity(intentCancelAllAccount);
//                MyUtils.myPendingTransitionLeftInRightOut(this);

                callAllAccountActivity("cancel");

                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_account, menu);
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
                onBackPressed();

                break;

            case R.id.action_tick:
                //this to create/edit the account with the user input
                //this will behave same as green color tick button
                callAllAccountActivity("menu");

                break;

            default:
                //add account, add member, report, help & logout handled here
                MyUtils.commonMenuActions(this, item);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void callAllAccountActivity(String from){

        if((toolbar.getTitle().toString().toLowerCase().trim().equals("add account")) && (!from.equals("cancel"))){
            MyUtils.showToast(this, "Account added successfully. Oops... no its WIP.....");
        }else if((toolbar.getTitle().toString().toLowerCase().trim().equals("edit account")) && (!from.equals("cancel"))){
            MyUtils.showToast(this, "Account updated successfully. Oops... no its WIP.....");
        }

        Intent intentCancelAllAccount = new Intent(this, AllAccountsActivity.class);
        startActivity(intentCancelAllAccount);
        MyUtils.myPendingTransitionLeftInRightOut(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        MyUtils.myPendingTransitionLeftInRightOut(this);
    }
}
