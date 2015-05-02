package com.bobcode.splitexpense.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.constants.Constants;
import com.bobcode.splitexpense.helpers.ChooseCategoryDialogFragment;
import com.bobcode.splitexpense.helpers.DateAndTimeHelper;
import com.bobcode.splitexpense.utils.MyUtils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

import java.util.Arrays;
import java.util.Calendar;


public class AddOREditEventActivity extends ActionBarActivity implements View.OnClickListener, OnDateSetListener {
    private Toolbar toolbar;

    private TextView textViewAEEHeader;
    private Spinner spinnerAEEAccountName;
    private EditText editTxtAEEventDate;
    private EditText textViewAEEDescription;
    private EditText editTxtAEEChooseCategory;
    private Spinner spinnerAEEWhoPaid;
    private ImageView currencyImage;
    private EditText editTxtAEEAmount;

    private ImageButton imgAEABtnOk;
    private ImageButton imgAEABtnCancel;

    private Bundle savedInstanceState;

    private String pickedDate;
    private String[] existingAccounts;
    private String[] members;

    //check all
    private TextView textViewAEECheckAll;

    //uneven split
    private TextView textViewAEEUnEvenSplit;

    private GridLayout gridLayoutAEEMember;

    private CheckBox[] checkBox;

    private String[] accountMembersList;

    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_event);

        this.savedInstanceState = savedInstanceState;

        //grid layout to add the members checkbox
        gridLayoutAEEMember = (GridLayout) findViewById(R.id.gridLayoutAEEMember);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.drawable.ic_menu_add_edit_event);

        //Now is is taking a static data
        //later this data should come from database table "account profile"
        //Existing Account Name spinner
        spinnerAEEAccountName = (Spinner) findViewById(R.id.spinnerForgotCredUserName);
        existingAccounts = getResources().getStringArray(R.array.existingAccounts);
        ArrayAdapter<String> arrayAdapterExistingAccounts = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.textViewSpinner, existingAccounts);
        arrayAdapterExistingAccounts.setDropDownViewResource(R.layout.spinner_item);
        spinnerAEEAccountName.setAdapter(arrayAdapterExistingAccounts);

        //Event Date Date picker
        //By default the current date will be populated
        //but if the user can able to change the date by clicking the date picker
        editTxtAEEventDate = (EditText) findViewById(R.id.editTxtAEEventDate);
        editTxtAEEventDate.setOnClickListener(this);

        //Event Description
        textViewAEEDescription = (EditText) findViewById(R.id.textViewAEEDescription);

        //Event Category Dialog
        editTxtAEEChooseCategory = (EditText) findViewById(R.id.editTxtAEEChooseCategory);
        editTxtAEEChooseCategory.setOnClickListener(this);

        //Event Who Paid spinner - it will list all the members
        spinnerAEEWhoPaid = (Spinner) findViewById(R.id.spinnerAEEWhoPaid);
        members = getResources().getStringArray(R.array.membersTemp);
        ArrayAdapter<String> arrayAdapterWhoPaid = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.textViewSpinner, members);
        arrayAdapterWhoPaid.setDropDownViewResource(R.layout.spinner_item);
        spinnerAEEWhoPaid.setAdapter(arrayAdapterWhoPaid);

        //event currency
        currencyImage = (ImageView) findViewById(R.id.currencyImage);

        //Event Amount
        editTxtAEEAmount = (EditText) findViewById(R.id.editTxtAEEAmount);

        String eventAction = getIntent().getStringExtra(Constants.EVENT_ACTION);
        if (eventAction != null) {
            eventAction = eventAction.toLowerCase();

            String accountName = getIntent().getStringExtra(Constants.ACCOUNT_NAME);
            String accountCurrency = getIntent().getStringExtra(Constants.ACCOUNT_CURRENCY);
            String accountMembers = getIntent().getStringExtra(Constants.ACCOUNT_MEMBERS);

            //populate Account Name based on All Account Intent account name data
            spinnerAEEAccountName.setSelection(Arrays.asList(existingAccounts).indexOf(accountName));

            //populate current symbol based on All Account Intent currency data
            String imgName = null;
            if (accountCurrency != null) {
                accountCurrency = accountCurrency.toLowerCase();

                if (accountCurrency.equals("us dollar")) {
                    imgName = "ic_currency_dollar";
                } else if (accountCurrency.equals("canadian dollar")) {
                    imgName = "ic_currency_dollar";
                } else if (accountCurrency.equals("british pound")) {
                    imgName = "ic_currency_pound";
                } else if (accountCurrency.equals("indian rupee")) {
                    imgName = "ic_currency_indian_rupee";
                } else if (accountCurrency.equals("euro")) {
                    imgName = "ic_currency_euro";
                } else if (accountCurrency.equals("australian dollar")) {
                    imgName = "ic_currency_dollar";
                }
                currencyImage.setImageResource(getResources().getIdentifier(imgName, "drawable", getPackageName()));
            }

////**************************************** Dynamically add check box for members ***************************
//        //display account members dynamically
//        //here the members list based on members of a particular account
//
//        //gridLayoutAEEMember.setRowCount(noOfRows);
//        gridLayoutAEEMember.setRowCount(6);
//        gridLayoutAEEMember.setColumnCount(2);
//
//        //dynamically load the check for each members
//        checkBox = MyUtils.loadMembersCheckbox(gridLayoutAEEMember, checkBox, members, this);
////**************************************** Dynamically add check box for members ***************************
            //pre-selected the members from the selected account information
            accountMembersList = accountMembers.split(",");
            int totalMembers = (int) accountMembersList.length;
            int noOfRows = 0;
            if ((totalMembers) % 2 >= 1) {
                noOfRows = (totalMembers / 2) + 1;
            } else {
                noOfRows = (totalMembers / 2);
            }
            gridLayoutAEEMember.setRowCount(noOfRows);
            gridLayoutAEEMember.setColumnCount(2);
            checkBox = MyUtils.loadMembersCheckbox(gridLayoutAEEMember, checkBox, accountMembersList, this);

            if (eventAction.equals("add")) {
                toolbar.setTitle("Add Event");

                editTxtAEEventDate.setText(DateAndTimeHelper.getRawCurrentDate());

                selectCheckBox(accountMembers);
            } else if (eventAction.equals("edit")) {
                toolbar.setTitle("Edit Event");

                String eventDate = getIntent().getStringExtra(Constants.EDIT_EVENT_DATE);
                String eventDescription = getIntent().getStringExtra(Constants.EDIT_EVENT_DESCRIPTION);
                String eventCategory = getIntent().getStringExtra(Constants.EDIT_EVENT_CATEGORY);
                String eventWhoPaid = getIntent().getStringExtra(Constants.EDIT_EVENT_WHO_PAID).toLowerCase().trim();
                String eventAmount = getIntent().getStringExtra(Constants.EDIT_EVENT_AMOUNT);
                String eventForWhom = getIntent().getStringExtra(Constants.EDIT_EVENT_FOR_WHOM);

                //setting event - date
                editTxtAEEventDate.setText(eventDate.replace(" ", "-"));

                //setting event - description
                textViewAEEDescription.setText(eventDescription);

                //setting event - category
                editTxtAEEChooseCategory.setText(eventCategory);

                //setting event  - who paid
                for (int index = 0; index < spinnerAEEWhoPaid.getAdapter().getCount(); index++) {
                    String currentSpinnerItem = spinnerAEEWhoPaid.getItemAtPosition(index).toString().toLowerCase().trim();
                    if (currentSpinnerItem.equals(eventWhoPaid)) {
                        spinnerAEEWhoPaid.setSelection(index);
                        break;
                    }
                }

                //setting event - amount
                editTxtAEEAmount.setText(eventAmount);

                //selecting event - for whom
                selectCheckBox(eventForWhom);
            }
        }

        textViewAEECheckAll = (TextView) findViewById(R.id.textViewAEECheckAll);
        textViewAEECheckAll.setOnClickListener(this);

        textViewAEEUnEvenSplit = (TextView) findViewById(R.id.textViewAEEUnEvenSplit);
        textViewAEEUnEvenSplit.setOnClickListener(this);

        imgAEABtnOk = (ImageButton) findViewById(R.id.imgAEABtnOk);
        imgAEABtnOk.setOnClickListener(this);

        imgAEABtnCancel = (ImageButton) findViewById(R.id.imgAEABtnCancel);
        imgAEABtnCancel.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void selectCheckBox(String membersToSelect) {
        String[] membersToSelectList = membersToSelect.split(",");
        for (int indexAccountMembers = 0; indexAccountMembers < membersToSelectList.length; indexAccountMembers++) {
            String memberTemp = membersToSelectList[indexAccountMembers].trim().toLowerCase();
            for (int indexCheckBox = 0; indexCheckBox < checkBox.length; indexCheckBox++) {
                String checkBoxMemberName = checkBox[indexCheckBox].getText().toString().trim().toLowerCase();
                if (checkBoxMemberName.equals(memberTemp)) {
                    checkBox[indexCheckBox].setChecked(true);
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editTxtAEEventDate:
                picDate(savedInstanceState);
                break;

            case R.id.editTxtAEEChooseCategory:
                //This is to choose a category for the event
                //The code will handle(remember the user choice) even if the user change the phone orientation
                //so that user do not want to make a selection again after the orientation change
                String currentCategory = editTxtAEEChooseCategory.getText().toString().trim();
                ChooseCategoryDialogFragment chooseCategoryDialogFragment = new ChooseCategoryDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.CATEGORY_TO_CHOOSE, currentCategory);
                chooseCategoryDialogFragment.setArguments(bundle);
                chooseCategoryDialogFragment.show(getSupportFragmentManager(), "choose category");

                break;

            case R.id.textViewAEECheckAll:
                if (checkBox.length >= 0) {
                    for (int index = 0; index < checkBox.length; index++) {
                        checkBox[index].setChecked(true);
                    }
                }
                break;

            case R.id.textViewAEEUnEvenSplit:
//----------------------- Pending -------------------------------------------//
                break;

            case R.id.imgAEABtnOk:
//----------------------- Pending -------------------------------------------//
                //validate below conditions
                //1) description should not be empty
                //2) category should be selected
                //3) amount should be >=1
                //4) at-least one member selected

                //Upon successful after the validation --
                // add the event to the selected account in "account_master" table
                //and navigate to "all account" activity
                from = "ok";
                onBackPressed();

                break;

            case R.id.imgAEABtnCancel:
                from = "cancel";
                onBackPressed();

                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_add_edit_event, menu);
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
                from = "cancel";
                onBackPressed();
                break;

            case R.id.action_tick:
                from = "tick";
                onBackPressed();
                break;

            default:
                //add account, add member, report, help & logout handled here
                MyUtils.commonMenuActions(this, item);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void picDate(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);

        datePickerDialog.setVibrate(false);
        //datePickerDialog.setYearRange(1985, 2037);
        datePickerDialog.setYearRange(2014, 2037);
        datePickerDialog.setCloseOnSingleTapDay(false);
        datePickerDialog.setStyle(R.style.ThemeOverlay_AppCompat_Light, android.R.style.Theme_Material_Light_Dialog);
        datePickerDialog.show(getSupportFragmentManager(), "datepicker");

        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag("datepicker");
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (from==null){
            from = "cancel";
        }
        if((toolbar.getTitle().toString().toLowerCase().trim().equals("add event")) && (!from.equals("cancel"))){
            MyUtils.showToast(this, "Event added successfully. Oops... no its WIP.....");
        }else if((toolbar.getTitle().toString().toLowerCase().trim().equals("edit event")) && (!from.equals("cancel"))){
            MyUtils.showToast(this, "Event updated successfully. Oops... no its WIP.....");
        }

        super.onBackPressed();

        MyUtils.myPendingTransitionLeftInRightOut(this);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        pickedDate = DateAndTimeHelper.month[month] + "-" + day + "-" + year;
        editTxtAEEventDate.setText(pickedDate);
    }
}

