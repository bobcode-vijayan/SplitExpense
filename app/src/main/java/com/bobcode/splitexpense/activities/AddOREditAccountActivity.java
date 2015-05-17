package com.bobcode.splitexpense.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.bobcode.splitexpense.helpers.DateAndTimeHelper;
import com.bobcode.splitexpense.helpers.SplitExpenseSQLiteHelper;
import com.bobcode.splitexpense.models.AccountProfileTableModel;
import com.bobcode.splitexpense.utils.MySharedPrefs;
import com.bobcode.splitexpense.utils.MyUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddOREditAccountActivity extends ActionBarActivity implements View.OnClickListener, View.OnTouchListener {
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

    private SplitExpenseSQLiteHelper splitExpenseSQLiteHelper;

    private boolean isAccountAddedOREditedSuccessfully;

    private String accountNameBeforeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_account);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        splitExpenseSQLiteHelper = new SplitExpenseSQLiteHelper(this);

        //Account Name
        editViewAEAAccountName = (EditText) findViewById(R.id.editViewAEAAccountName);


        //This is to auto complete text for account description
        //When ever user create an account the description will be saved in preference
        //When a user create a new account and while entering description, user will get the
        //all saved description from preference to select to avoid typing
        MySharedPrefs mySharedPrefs = new MySharedPrefs((Activity) this);
        String tempDescription = mySharedPrefs.getDataFromSharePrefs(mySharedPrefs.PREFS_KEY_FOR_ADD_ACCOUNT_DESCRIPTION).trim();
        String accountDescriptionAutoList[] = null;
        if (!((tempDescription.length() == 0) || (tempDescription.equals("key not found")))) {
            accountDescriptionAutoList = tempDescription.split(",");
        }

        textViewAEADescriptionAutoComplete = (AutoCompleteTextView) findViewById(R.id.textViewAEADescriptionAutoComplete);
        ArrayAdapter<String> arrayAdapterForAutoComplete = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accountDescriptionAutoList);
        textViewAEADescriptionAutoComplete.setAdapter(arrayAdapterForAutoComplete);
        textViewAEADescriptionAutoComplete.setThreshold(1);


        //This is a static supported currency list
        //this data is stored in <string-array name="currencyList">
        final String[] currencyListArray = getResources().getStringArray(R.array.currencyList);
        //currency spinner
        spinnerAEACurrency = (Spinner) findViewById(R.id.spinnerAEACurrency);
        ArrayAdapter<String> arrayAdapterForCurrency = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.textViewSpinner, currencyListArray);
        arrayAdapterForCurrency.setDropDownViewResource(R.layout.spinner_item);
        spinnerAEACurrency.setAdapter(arrayAdapterForCurrency);


        //This is a static account status
        //it might be New, Active, Pending or Settled
        //this data is stored in <string-array name="accountStatus">
        final String[] accountStatusArray = getResources().getStringArray(R.array.accountStatus);
        //account status spinner
        spinnerAEAStatus = (Spinner) findViewById(R.id.spinnerAEAStatus);
        ArrayAdapter<String> arrayAdapterForAccountStatus = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.textViewSpinner, accountStatusArray);
        arrayAdapterForAccountStatus.setDropDownViewResource(R.layout.spinner_item);
        spinnerAEAStatus.setAdapter(arrayAdapterForAccountStatus);

//**************************************** Dynamically add check box for members ***************************
        //Add all the added account member dynamically from "members_profile" table

        //grid layout to add the members checkbox
        gridLayoutAEAMember = (GridLayout) findViewById(R.id.gridLayoutAEAMember);

        //get all the added member display name
        List<String> allMembersDisplayList = splitExpenseSQLiteHelper.getDisplayNameOfAllMembers();

        //get the number of members and calculate the number of row in grid layout to display
        //checkbox and member name
        int totalMembers = allMembersDisplayList.size();
        int noOfRows = 0;
        if ((totalMembers) % 2 >= 1) {
            noOfRows = (totalMembers / 2) + 1;
        } else {
            noOfRows = (totalMembers / 2);
        }
        gridLayoutAEAMember.setRowCount(noOfRows);
        gridLayoutAEAMember.setColumnCount(2);

        //dynamically load the check for each members
        checkBox = MyUtils.loadMembersCheckbox(gridLayoutAEAMember, checkBox, allMembersDisplayList, this);

//**************************************** Dynamically add check box for members ***************************

        //reading the data passed by intent to add an account
        String accountAction = getIntent().getStringExtra(Constants.ACCOUNT_ACTION);
        //if the data is not null, convert it to lower case
        if (accountAction != null) {
            accountAction = accountAction.toLowerCase();
        }

        if (accountAction.equals("edit")) {
            toolbar.setTitle("Edit Account");

            String accountName = getIntent().getStringExtra(Constants.ACCOUNT_NAME);
            accountNameBeforeEdit = accountName;
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

            try {
                //pre-selected the members from the selected account information
                String[] selectedAccountMembers = accountMembers.split(",");

//************************** Pending (may be unnecessary check)************************************************
                if (selectedAccountMembers.length > checkBox.length) {
                    MyUtils.showToast(this, "some member are deleted from member_profile");
                }
//************************** Pending (may be unnecessary check)************************************************

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
            } catch (Exception e) {
                e.printStackTrace();
                MyUtils.showToast(this, "some member are deleted from member_profile");
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

        textViewAEADescriptionAutoComplete.setOnTouchListener(this);
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
                //this the tick(Green color) button to add/edit the account with the user input

                //validate "account name" and description field are not empty
                //validate at least one member has been selected
                //if the above validation failed, show appropriate toast message
                //else add the account to "account_profile" table

                //code for adding an account
                addOREditAAccount();

                if (isAccountAddedOREditedSuccessfully) {
                    callAllAccountActivity("ok");
                }

                break;

            case R.id.imgAEABtnCancel:
                callAllAccountActivity("cancel");

                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.textViewAEADescriptionAutoComplete:
                MyUtils.showKeyboard(this, textViewAEADescriptionAutoComplete);

                //read the user input
                String strAccountName = editViewAEAAccountName.getText().toString().trim();

                //validate user name min char
                if (strAccountName.isEmpty()) {
                    MyUtils.showToast(this, "enter account name before description");
                } else if (!MyUtils.minCharCheck(strAccountName, Constants.ACCOUNT_NAME_MIN_LENGTH)) {
                    MyUtils.showToast(this, "account name minimum char should be 6");
                } else {
                    textViewAEADescriptionAutoComplete.setFocusable(true);
                    textViewAEADescriptionAutoComplete.requestFocus();
                }

                return true;
        }
        return true;
    }

    //This function is to add a new account
    public void addOREditAAccount() {
        //validate "account name" and description field are not empty
        //validate at least one member has been selected
        //if the above validation failed, show appropriate toast message
        //else add the account to "account_profile" table

        String successMessage = "";

        try {
            //account_name validation
            String strAccountName = editViewAEAAccountName.getText().toString().trim();
            if (strAccountName.isEmpty()) {
                MyUtils.showToast(this, "account name should not be blank");
                editViewAEAAccountName.setFocusable(true);
                editViewAEAAccountName.requestFocus();
                return;
            } else if (!MyUtils.minCharCheck(strAccountName, Constants.ACCOUNT_NAME_MIN_LENGTH)) {
                MyUtils.showToast(this, "account name minimum char should be 6");
                editViewAEAAccountName.setFocusable(true);
                editViewAEAAccountName.requestFocus();
                return;
            }else if(! strAccountName.equals(accountNameBeforeEdit)){
                //This is to enable the user to edit the existing account detail even without any change in the account name
                //and if any change in the account name, ensure the name is not already exists in "account_profile" table
                List<String> allAccountNameList = new ArrayList<>();
                allAccountNameList = splitExpenseSQLiteHelper.getAccountNameOfAllAccount();
                for (String tempAccountName : allAccountNameList) {
                    if (tempAccountName.equals(strAccountName)) {
                        MyUtils.showToast(this, "account already exits");
                        return;
                    }
                }
            }

            //description
            String strDescription = textViewAEADescriptionAutoComplete.getText().toString().trim();
            if (strDescription.isEmpty()) {
                MyUtils.showToast(this, "description should not be blank");
                textViewAEADescriptionAutoComplete.setFocusable(true);
                textViewAEADescriptionAutoComplete.requestFocus();
                return;
            } else if (!MyUtils.minCharCheck(strDescription, Constants.ACCOUNT_DESCRIPTION_MIN_LENGTH)) {
                MyUtils.showToast(this, "description minimum char should be 6");
                textViewAEADescriptionAutoComplete.setFocusable(true);
                textViewAEADescriptionAutoComplete.requestFocus();
                return;
            }

            //currency & status field validation is not required since it will populate default value and
            //user can not make these field blank manually
            String strCurrency = spinnerAEACurrency.getSelectedItem().toString().trim();
            String strStatus = spinnerAEAStatus.getSelectedItem().toString().trim();

            //members
            int membersCount = splitExpenseSQLiteHelper.getMembersCount();
            int noOfSelectedMember = 0;
            String selectedMembers = "";
            String tempEndChar = "";
            for (int index = 0; index < membersCount; index++) {
                if (checkBox[index].isChecked()) {
                    //this is to add , between each number and ensure no , at the beginning/end
                    if (noOfSelectedMember >= 1) {
                        tempEndChar = ", ";
                    } else {
                        tempEndChar = "";
                    }
                    selectedMembers = selectedMembers + tempEndChar + checkBox[index].getText().toString();

                    noOfSelectedMember += 1;
                }
            }
            if (noOfSelectedMember == 0) {
                MyUtils.showToast(this, "at least one member should be selected to add a account");
                return;
            }

            //created_On & last_Modified
            //getting the current date. this is to update the "last modified" column in "member_profile" table
            String todayDate = DateAndTimeHelper.getRawCurrentDate();
            todayDate = todayDate.replace(" ", "-");

            //read toolbar title to know add or edit action
            if (toolbar.getTitle().toString().toLowerCase().trim().equals("add account")) {
                //call addAnAccount function in SplitExpenseSQLiteHelper.java to add a account
                AccountProfileTableModel accountProfileTableModel = new AccountProfileTableModel(strAccountName, strDescription, strCurrency, strStatus, selectedMembers, todayDate, todayDate);
                splitExpenseSQLiteHelper.addAnAccount(accountProfileTableModel);
                successMessage = "account added successfully";

            } else if (toolbar.getTitle().toString().toLowerCase().trim().equals("edit account")) {
                //call updateAnAccount function in SplitExpenseSQLiteHelper.java to add a account
                AccountProfileTableModel accountProfileTableModelToUpdate = new AccountProfileTableModel(strAccountName, strDescription, strCurrency, strStatus, selectedMembers, todayDate);
                splitExpenseSQLiteHelper.updateAnAccount(accountNameBeforeEdit, accountProfileTableModelToUpdate);
                successMessage = "account edited successfully";
            }

            MyUtils.showToast(this, successMessage);

            isAccountAddedOREditedSuccessfully = true;

            //Adding description to shared prefs
            MySharedPrefs mySharedPrefs = new MySharedPrefs((Activity) this);
            String tempDescription = mySharedPrefs.getDataFromSharePrefs(mySharedPrefs.PREFS_KEY_FOR_ADD_ACCOUNT_DESCRIPTION).trim();
            String descriptionToUpdate = "";
            if ((tempDescription.length() == 0) || (tempDescription.equals("key not found"))) {
                descriptionToUpdate = strDescription;
            } else {
                String tempDescriptionLower = tempDescription.trim().toLowerCase();
                String strDescriptionLower = strDescription.trim().toLowerCase();
                if (!tempDescriptionLower.contains(strDescriptionLower)) {
                    descriptionToUpdate = tempDescription + "," + strDescription;
                } else {
                    descriptionToUpdate = tempDescription;
                }
            }
            mySharedPrefs.storeDataToSharePrefs(mySharedPrefs.PREFS_KEY_FOR_ADD_ACCOUNT_DESCRIPTION, descriptionToUpdate);

        } catch (Exception e) {
            e.printStackTrace();

            MyUtils.showToast(this, "error adding/editing account");

            isAccountAddedOREditedSuccessfully = false;
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
                addOREditAAccount();

                //Validate Add/Edit account is success. Navigate to All Account Activity
                if (isAccountAddedOREditedSuccessfully) {
                    callAllAccountActivity("menu");
                }

                break;

            default:
                //add account, add member, report, help & logout handled here
                MyUtils.commonMenuActions(this, item);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //This function is to finish the current activity and navigate to All Account activity
    public void callAllAccountActivity(String from) {
        finish();

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
