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
import com.bobcode.splitexpense.helpers.SplitExpenseSQLiteHelper;
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

    private TextView textViewErrorMessage;

    private FloatingActionButton fabtnAddAccount;

    private boolean isMemberExists;
    private boolean isAccountExits;

    private SplitExpenseSQLiteHelper splitExpenseSQLiteHelper;

    private Menu myMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_accounts);

        //This is to display no account exists message if no account exits
        //when user navigate to all account page
        textViewErrorMessage = (TextView) findViewById(R.id.textViewErrorMessage);

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

//************************ Recycler view to display all the available account ****************************
        //This is to populated all available accounts in the app
        populateAccounts();

        fabtnAddAccount.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //This is to populated all available accounts in the app
        populateAccounts();
    }

    //This function is to populated all available accounts in the app
    public void populateAccounts(){
        //               validation - I
        //validate if any members exists in "member_profile"
        //if no member exists make add a account menu item invisible and display a message to
        //user to add a member before creating an account update isMemberExits to false
        //else if member exists, make add a account menu item visible  and
        //move to validation-II update isMemberExits to true

        //validate if any member exits in "members profile" table
        //if any member exits display all the members details from the table
        //else display the text message to the user to create an member to begin with
        splitExpenseSQLiteHelper = new SplitExpenseSQLiteHelper(this);
        int totalMembers = splitExpenseSQLiteHelper.getMembersCount();
        if(totalMembers >=1){
            isMemberExists = true;
            if(myMenu != null){
                myMenu.findItem(R.id.action_add_account).setVisible(true);
            }
        }else if(totalMembers == 0){
            isMemberExists = false;
            if(myMenu != null){
                myMenu.findItem(R.id.action_add_account).setVisible(false);
            }
        }

        if (isMemberExists == false) {
            textViewErrorMessage.setText(R.string.no_member_exist);
            textViewErrorMessage.setVisibility(View.VISIBLE);
            recyclerViewAllAccounts.setVisibility(View.INVISIBLE);
        } else {
            //               validation - II
            //validate if any account exits in "account_profile" table
            //if any account exits display all the accounts details from the table and update isAccountExits to true
            //else display the appropriate text message(toast) to user and update isAccountExits to false

            recyclerViewAllAccounts.setVisibility(View.VISIBLE);

            splitExpenseSQLiteHelper = new SplitExpenseSQLiteHelper(this);
            int totalAccounts = splitExpenseSQLiteHelper.getAccountProfileAccountCount();
            if(totalAccounts >= 1){
                isAccountExits = true;
                textViewErrorMessage.setVisibility(View.INVISIBLE);
            }else if(totalAccounts == 0){
                isAccountExits = false;
                textViewErrorMessage.setVisibility(View.VISIBLE);
            }
            if (isAccountExits) {
                //Data (get all the account details from database)
                accountSummaryModelList = new ArrayList<>();
                accountSummaryModelList = splitExpenseSQLiteHelper.getAllAccounts();
                AccountSummaryAdapter accountSummaryAdapter = new AccountSummaryAdapter(this, (ArrayList) accountSummaryModelList);

                recyclerViewAllAccounts.setAdapter(accountSummaryAdapter);
            } else {
                textViewErrorMessage.setText(R.string.no_account_exist);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtnAllAccounts:
                if (isMemberExists == false) {
                    //fabtnAllAccounts button should call the add member activity while clicked
                    Intent intentAddMember = new Intent(this, AddOREditMemberActivity.class);
                    intentAddMember.putExtra(Constants.MEMBER_ACTION, "Add");
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
        myMenu = menu;

        if (isMemberExists == false) {
            //menu.findItem(R.id.action_add_account).setEnabled(false);
            menu.findItem(R.id.action_add_account).setVisible(false);
        }else{
            menu.findItem(R.id.action_add_account).setVisible(true);
        }
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
                MyUtils.myPendingTransitionRightInLeftOut(this);

                break;

            default:
                //add account, add member, report, help & logout handled here
                MyUtils.commonMenuActions(this, item);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
