package com.bobcode.splitexpense.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.adapters.AccountSummaryAdapter;
import com.bobcode.splitexpense.constants.Constants;
import com.bobcode.splitexpense.models.AccountSummaryModel;
import com.bobcode.splitexpense.utils.MyUtils;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijayananjalees on 4/16/15.
 */
public class AllAccountsActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private RecyclerView recyclerViewAllAccounts;

    private List<AccountSummaryModel> accountSummaryModelList;

    private TextView textViewNoAccountExists;

    private FloatingActionButton fabtnAddAccount;

    private boolean isMemberExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_accounts);

        //This is to display no account exists message if no account exits
        //when user navigate to all account page
        textViewNoAccountExists = (TextView) findViewById(R.id.textViewNoAccountExists);

        //floating action button to add an account
        fabtnAddAccount = (FloatingActionButton) findViewById(R.id.fabtnAllAccounts);

        //This is to setup the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("All Accounts");
        setSupportActionBar(toolbar);

//************************ Recycler view to display all the available account ****************************
        recyclerViewAllAccounts = (RecyclerView) findViewById(R.id.recyclerViewForAllAccounts);
        recyclerViewAllAccounts.setHasFixedSize(true);
        fabtnAddAccount.attachToRecyclerView(recyclerViewAllAccounts);

        //RecyclerView requires a layout manager
        //This component positions item views inside the row and determines when it is time to
        // recycle the views
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAllAccounts.setLayoutManager(linearLayoutManager);

//------------------------------- Pending -------------------------------------------------------
        //               validation - I
        //validate if any members exists in "member_profile"
        //if no member exists display a message to user to add a member before creating an account
        //          update isMemberExits to false
        //else if member exists, move to validation-II
        //          update isMemberExits to true
        isMemberExists = true;
        if (isMemberExists == false) {
            textViewNoAccountExists.setText(R.string.no_member_exist);
            textViewNoAccountExists.setVisibility(View.VISIBLE);
        } else {
            //               validation - II
            //validate if any account exits in "account_profile" table
            //if any account exits display all the accounts details from the table
            //          update isAccountExits to true
            //else display the text message to the user to create an account to begin with
            //          update isAccountExits to false
            boolean isAccountExits = true;
            if (isAccountExits) {
                //Data

                AccountSummaryModel febAccount = new AccountSummaryModel("Feb 2015", "Vijayan, Senthil, Amitesh, Venky, Vijay Sridhar, Shanmugam", "Feb Room monthly expense", "Sunday, Feb 01 2015", 109, 810.00f, "Settled", "US Dollar");
                AccountSummaryModel marchAccount = new AccountSummaryModel("March 2015", "Vijayan, Senthil, Amitesh, Venky, Vijay Sridhar, Shanmuga, Arun", "March Room monthly expense", "Monday, March 02 2015", 87, 620.00f, "Pending", "US Dollar");
                AccountSummaryModel aprilAccount = new AccountSummaryModel("April 2015", "Vijayan, Senthil, Amitesh, Venky, Vijay Sridhar, Shanmuga, Arun", "April Room monthly expense", "Tuesday, April 02 2015", 139, 1893.29f, "Active", "Indian Rupee");
                AccountSummaryModel mayAccount = new AccountSummaryModel("May 2015", "Vijayan, Senthil, Amitesh, Venky, Vijay Sridhar, Shanmuga, Arun", "May Room monthly expense", "Wednesday, May 02 2015", 10, 210.00f, "Active", "British Pound");
                AccountSummaryModel AugustAccount = new AccountSummaryModel("August 2015", "Vijayan, Senthil, Amitesh", "August Room monthly expense", "Wednesday, August 02 2015", 10, 210.00f, "Active", "British Pound");

                accountSummaryModelList = new ArrayList<>();
                accountSummaryModelList.add(febAccount);
                accountSummaryModelList.add(marchAccount);
                accountSummaryModelList.add(aprilAccount);
                accountSummaryModelList.add(mayAccount);
                accountSummaryModelList.add(AugustAccount);

                AccountSummaryAdapter accountSummaryAdapter = new AccountSummaryAdapter(this, (ArrayList) accountSummaryModelList);

                recyclerViewAllAccounts.setAdapter(accountSummaryAdapter);
            } else {
                textViewNoAccountExists.setText(R.string.no_account_exist);
                textViewNoAccountExists.setVisibility(View.VISIBLE);
            }
        }
//------------------------------- Pending -------------------------------------------------------
//************************ Recycler view to display all the available account ****************************

        fabtnAddAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtnAllAccounts:
                if (isMemberExists == false) {
                    //fabtnAllAccounts button should call the add member activity while clicked
                    Intent intentAddMember = new Intent(this, AddOREditMemberActivity.class);
                    startActivity(intentAddMember);
                } else {
                    //fabtnAllAccounts button should call the add account activity while clicked
                    Intent intentAddAccount = new Intent(this, AddOREditAccountActivity.class);
                    intentAddAccount.putExtra(Constants.ACCOUNT_ACTION, "ADD");
                    intentAddAccount.putExtra(Constants.ADD_ACCOUNT_ACTION_SOURCE, "FABAllACCOUNTS");
                    startActivity(intentAddAccount);
                }

                MyUtils.myPendingTransitionRightInLeftOut(this);

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_accounts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_report:
                Intent intentReport = new Intent(this, PayoutActivity.class);
                startActivity(intentReport);

                break;

            default:
                //add account icon click handled here
                //add member icon click handled here
                MyUtils.commonMenuActions(this, item);

                break;
        }

        MyUtils.myPendingTransitionRightInLeftOut(this);
        return super.onOptionsItemSelected(item);
    }
}
