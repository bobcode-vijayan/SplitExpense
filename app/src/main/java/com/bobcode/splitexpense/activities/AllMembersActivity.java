package com.bobcode.splitexpense.activities;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.bobcode.splitexpense.adapters.MembersDetailsAdapter;
import com.bobcode.splitexpense.constants.Constants;
import com.bobcode.splitexpense.helpers.SplitExpenseSQLiteHelper;
import com.bobcode.splitexpense.models.MemberDetailModel;
import com.bobcode.splitexpense.models.MemberProfileModel;
import com.bobcode.splitexpense.utils.MyUtils;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class AllMembersActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private FloatingActionButton fabtnAddMember;

    private TextView textViewNoMemberExists;

    private RecyclerView cardListForAllMembers;

    private List<MemberDetailModel> memberDetailModelList;

    private SplitExpenseSQLiteHelper splitExpenseSQLiteHelper;

    private List<MemberProfileModel> allAddedMembersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_members);

        //Retrieve the user clicked account name and set the name as toolbar title
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("All Members");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //This is to display a no member exists message if no member exits
        // when user navigate to all member page
        textViewNoMemberExists = (TextView) findViewById(R.id.textViewNoMemberExists);

        //floating action button to add an event
        fabtnAddMember = (FloatingActionButton) findViewById(R.id.fabtnAddMember);
        fabtnAddMember.setOnClickListener(this);

//************************ Recycler view to display all the available members ****************************
        cardListForAllMembers = (RecyclerView) findViewById(R.id.cardListForAllMembers);
        cardListForAllMembers.setHasFixedSize(true);
        fabtnAddMember.attachToRecyclerView(cardListForAllMembers);

        //RecyclerView requires a layout manager
        //This component positions item views inside the row and determines when it is time to
        // recycle the views
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cardListForAllMembers.setLayoutManager(linearLayoutManager);

        //validate if any member exits in "members profile" table
        //if any member exits display all the members details from the table
        //else display the text message to the user to create an member to begin with
        splitExpenseSQLiteHelper = new SplitExpenseSQLiteHelper(this);
        int totalMembers = splitExpenseSQLiteHelper.getMembersCount();
        if(totalMembers >=1){
            allAddedMembersList = splitExpenseSQLiteHelper.getAllMembers();
            memberDetailModelList = new ArrayList<>();
            for (MemberProfileModel currentMemberDetail : allAddedMembersList) {
                Bitmap photo = currentMemberDetail.getPhoto();
                String currentName = currentMemberDetail.getName().trim();
                String currentDisplayName = currentMemberDetail.getDisplayName().trim();
                String currentComments = currentMemberDetail.getComments().trim();
                MemberDetailModel member = new MemberDetailModel(photo, currentName, currentDisplayName, currentComments);
                memberDetailModelList.add(member);
            }

            MembersDetailsAdapter membersDetailsAdapter = new MembersDetailsAdapter(this, (ArrayList) memberDetailModelList);
            cardListForAllMembers.setAdapter(membersDetailsAdapter);
        }else if(totalMembers ==0){
            textViewNoMemberExists.setVisibility(View.VISIBLE);
        }
//************************ Recycler view to display all the available members ****************************

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtnAddMember:
                finish();
                Intent intentAddMember = new Intent(this, AddOREditMemberActivity.class);
                intentAddMember.putExtra(Constants.MEMBER_ACTION, "Add");
                startActivity(intentAddMember);
                MyUtils.myPendingTransitionRightInLeftOut(this);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_members, menu);
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

            case R.id.action_all_accounts:
                Intent intentAllAccounts = new Intent(this, AllAccountsActivity.class);
                intentAllAccounts.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentAllAccounts);

                MyUtils.myPendingTransitionRightInLeftOut(this);
                break;

            default:
                //add account, add member, report, help & logout handled here
                MyUtils.commonMenuActions(this, item);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        MyUtils.myPendingTransitionLeftInRightOut(this);
    }
}
